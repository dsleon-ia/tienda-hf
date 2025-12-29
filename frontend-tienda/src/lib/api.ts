import { Category, Product, AuditLog, PaginatedResponse, ProductFilters } from '@/types';

const API_BASE = '/api';

async function fetchApi<T>(endpoint: string, options?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE}${endpoint}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options?.headers,
    },
    ...options,
  });

  if (!response.ok) {
    let errorMessage = `HTTP ${response.status}`;
    try {
      const text = await response.text();
      try {
        const json = JSON.parse(text);
        errorMessage = json.message || json.error || errorMessage;
      } catch {
        // If not JSON, use the text body if it's not too long
        if (text && text.length < 200) errorMessage = text;
      }
    } catch {
      // Ignore reading errors
    }
    throw new Error(errorMessage);
  }

  // Handle 204 No Content
  if (response.status === 204) {
    return {} as T;
  }

  // Safely read body for other statuses (like 200 OK with empty body)
  const text = await response.text();
  if (!text) {
    return {} as T;
  }

  try {
    return JSON.parse(text);
  } catch {
    console.warn("Response was not JSON:", text);
    return {} as T;
  }
}

// Categories API
export const categoriesApi = {
  getAll: () => fetchApi<Category[]>('/categories'),
  getById: (id: string) => fetchApi<Category>(`/categories/${id}`),
  create: (data: { name: string }) => 
    fetchApi<Category>('/categories', { method: 'POST', body: JSON.stringify(data) }),
  update: (id: string, data: { name: string }) => 
    fetchApi<Category>(`/categories/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id: string) => 
    fetchApi<void>(`/categories/${id}`, { method: 'DELETE' }),
};

// Products API
export const productsApi = {
  getAll: (filters?: ProductFilters) => {
    // If specific filters are present that require dedicated endpoints, use them.
    // Note: The backend has separate endpoints for search, category, and price range.
    // It does NOT support combining them in a single /products call based on the provided API docs.
    // The UI should ideally handle this logic, but we can do a best-effort here.
    
    if (filters?.search) {
      return productsApi.search(filters.search, filters.page, filters.size);
    }
    if (filters?.category) {
      return productsApi.getByCategory(filters.category, filters.page, filters.size);
    }
    if (filters?.minPrice !== undefined || filters?.maxPrice !== undefined) {
      return productsApi.getByPriceRange(filters.minPrice || 0, filters.maxPrice || 1000000, filters.page, filters.size);
    }

    // Default: list all
    const params = new URLSearchParams();
    if (filters?.page !== undefined) params.set('page', filters.page.toString());
    if (filters?.size) params.set('size', filters.size.toString());
    const query = params.toString() ? `?${params}` : '';
    return fetchApi<PaginatedResponse<Product>>(`/products${query}`);
  },
  
  getById: (id: string) => fetchApi<Product>(`/products/${id}`),
  
  // Adjusted to match CreateProductRequest
  create: (data: any) => 
    fetchApi<Product>('/products', { method: 'POST', body: JSON.stringify(data) }),
    
  update: (id: string, data: any) => 
    fetchApi<Product>(`/products/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
    
  delete: (id: string) => 
    fetchApi<void>(`/products/${id}`, { method: 'DELETE' }),
    
  updateStock: (id: string, stock: number) => 
    fetchApi<Product>(`/products/${id}/stock`, { method: 'PATCH', body: JSON.stringify({ stock }) }),
    
  getByCategory: (categoryId: string, page = 0, size = 10) => 
    fetchApi<PaginatedResponse<Product>>(`/products/category/${categoryId}?page=${page}&size=${size}`),
    
  search: (query: string, page = 0, size = 10) => 
    fetchApi<PaginatedResponse<Product>>(`/products/search?q=${encodeURIComponent(query)}&page=${page}&size=${size}`),
    
  getByPriceRange: (min: number, max: number, page = 0, size = 10) => 
    fetchApi<PaginatedResponse<Product>>(`/products/price-range?min=${min}&max=${max}&page=${page}&size=${size}`),
};

// Audit API
export const auditApi = {
  getByProductId: (productId: string) => 
    fetchApi<AuditLog[]>(`/audit/products/${productId}`),
  getLatest: () => fetchApi<AuditLog[]>('/audit/products'),
  getByAction: (action: string) => 
    fetchApi<AuditLog[]>(`/audit/actions/${action}`),
};
