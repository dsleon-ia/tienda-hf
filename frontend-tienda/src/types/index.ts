export interface Category {
  id: string;
  name: string;
  productCount?: number;
}

export interface Rating {
  rate: number;
  count: number;
}

export interface Product {
  id: string;
  title: string;
  description: string;
  price: number;
  stock: number;
  categoryId?: string;
  categoryName?: string;
  // UI might expect category object, we can keep it optional or map it
  category?: Category; 
  image?: string;
  rating?: Rating;
  deleted?: boolean;
}

export interface AuditLog {
  id: string;
  productId: string;
  action: 'CREATE' | 'UPDATE' | 'DELETE' | 'STOCK_UPDATE';
  timestamp: string;
  details: Record<string, unknown>;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface ProductFilters {
  category?: string;
  search?: string;
  minPrice?: number;
  maxPrice?: number;
  page?: number;
  size?: number;
}
