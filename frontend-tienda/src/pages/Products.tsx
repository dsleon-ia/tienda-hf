import { useState } from 'react';
import { Header } from '@/components/layout/Header';
import { DataTable, Column } from '@/components/ui/data-table';
import { Button } from '@/components/ui/button';
import { BadgeStatus } from '@/components/ui/badge-status';
import { ProductForm } from '@/components/products/ProductForm';
import { StockDialog } from '@/components/products/StockDialog';
import { ProductFilters } from '@/components/products/ProductFilters';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { useProduct, useProducts,
  useCreateProduct,
  useUpdateProduct,
  useDeleteProduct,
  useUpdateStock,
  useSearchProducts,
  useProductsByCategory,
  useProductsByPriceRange,
} from '@/hooks/use-products';
import { useCategories } from '@/hooks/use-categories';
import { Product } from '@/types';
import { ProductDetailsDialog } from '@/components/products/ProductDetailsDialog';
import { Eye, Plus, MoreHorizontal, Pencil, Trash2, Package as PackageIcon, ChevronLeft, ChevronRight } from 'lucide-react';

export default function Products() {
  const [page, setPage] = useState(0);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isStockOpen, setIsStockOpen] = useState(false);
  const [isDetailsOpen, setIsDetailsOpen] = useState(false);
  const [viewProductId, setViewProductId] = useState<string | null>(null);
  const [editProductId, setEditProductId] = useState<string | null>(null);

  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null);
  const [deleteProduct, setDeleteProduct] = useState<Product | null>(null);
  
  // Filter states
  const [searchQuery, setSearchQuery] = useState('');
  const [categoryFilter, setCategoryFilter] = useState<string | null>(null);
  const [priceRange, setPriceRange] = useState<{ min: number | null; max: number | null }>({ min: null, max: null });

  const { data: categories = [] } = useCategories();
  
  // Base products query
  const { data: productsData, isLoading: productsLoading } = useProducts({ page, size: 10 });
  
  // Filtered queries
  const { data: searchResults, isLoading: searchLoading } = useSearchProducts(searchQuery);
  const { data: categoryResults, isLoading: categoryLoading } = useProductsByCategory(categoryFilter || '');
  const { data: priceResults, isLoading: priceLoading } = useProductsByPriceRange(
    priceRange.min || 0,
    priceRange.max || 999999
  );

  // Determine which data to display
  let displayProducts: Product[] = [];
  let isLoading = productsLoading;
  let totalPages = productsData?.totalPages || 1;

  if (searchQuery && searchQuery.length >= 2) {
    displayProducts = searchResults?.content || [];
    isLoading = searchLoading;
    totalPages = searchResults?.totalPages || 1;
  } else if (categoryFilter) {
    displayProducts = categoryResults?.content || [];
    isLoading = categoryLoading;
    totalPages = categoryResults?.totalPages || 1;
  } else if (priceRange.min !== null && priceRange.max !== null) {
    displayProducts = priceResults?.content || [];
    isLoading = priceLoading;
    totalPages = priceResults?.totalPages || 1;
  } else {
    displayProducts = productsData?.content || [];
  }

  const { data: viewProduct, isLoading: viewLoading } = useProduct(viewProductId || '');
  const { data: editProduct, isLoading: editLoading } = useProduct(editProductId || '');

  const createMutation = useCreateProduct();
  const updateMutation = useUpdateProduct();
  const deleteMutation = useDeleteProduct();
  const stockMutation = useUpdateStock();

  const handleCreate = () => {
    setEditProductId(null);
    setIsFormOpen(true);
  };

  const handleEdit = (product: Product) => {
    setEditProductId(product.id);
    setIsFormOpen(true);
  };

  const handleViewDetails = (product: Product) => {
    setViewProductId(product.id);
    setIsDetailsOpen(true);
  };

  const handleStock = (product: Product) => {
    setSelectedProduct(product);
    setIsStockOpen(true);
  };

  const handleSubmit = (data: any) => {
    const productData = {
      ...data,
      // categoryId is already in data from the form
    };

    if (editProductId) {
      updateMutation.mutate(
        { id: editProductId, data: productData },
        { onSuccess: () => setIsFormOpen(false) }
      );
    } else {
      createMutation.mutate(productData, { onSuccess: () => setIsFormOpen(false) });
    }
  };

  const handleStockSubmit = (stock: number) => {
    if (selectedProduct) {
      stockMutation.mutate(
        { id: selectedProduct.id, stock },
        { onSuccess: () => setIsStockOpen(false) }
      );
    }
  };

  const handleDelete = () => {
    if (deleteProduct) {
      deleteMutation.mutate(deleteProduct.id, {
        onSuccess: () => setDeleteProduct(null),
      });
    }
  };

  const handleSearch = (query: string) => {
    setSearchQuery(query);
    setCategoryFilter(null);
    setPriceRange({ min: null, max: null });
    setPage(0);
  };

  const handleCategoryFilter = (categoryId: string | null) => {
    setCategoryFilter(categoryId);
    setSearchQuery('');
    setPriceRange({ min: null, max: null });
    setPage(0);
  };

  const handlePriceFilter = (min: number | null, max: number | null) => {
    if (min !== null && max !== null && max > min) {
      setPriceRange({ min, max });
      setSearchQuery('');
      setCategoryFilter(null);
      setPage(0);
    } else {
      setPriceRange({ min: null, max: null });
    }
  };

  const columns: Column<Product>[] = [
    {
      key: 'product',
      header: 'Producto',
      cell: (product) => (
        <div className="flex items-center gap-3">
          <div className="h-12 w-12 rounded-lg bg-secondary flex items-center justify-center overflow-hidden">
            {product.image ? (
              <img src={product.image} alt={product.title} className="h-full w-full object-cover" />
            ) : (
              <PackageIcon className="h-6 w-6 text-muted-foreground" />
            )}
          </div>
          <div className="min-w-0 flex-1">
            <p className="font-medium text-foreground truncate" title={product.title}>{product.title}</p>
            <p className="text-xs text-muted-foreground">{product.categoryName || product.category?.name}</p>
          </div>
        </div>
      ),
    },
    {
      key: 'price',
      header: 'Precio',
      cell: (product) => (
        <span className="font-semibold text-foreground">${product.price.toFixed(2)}</span>
      ),
    },
    {
      key: 'stock',
      header: 'Stock',
      cell: (product) => (
        <BadgeStatus status={product.stock > 10 ? 'success' : product.stock > 0 ? 'warning' : 'error'}>
          {product.stock} unidades
        </BadgeStatus>
      ),
    },
    {
      key: 'actions',
      header: '',
      cell: (product) => (
        <div className="flex items-center gap-2">
          <Button 
            variant="ghost" 
            size="icon" 
            onClick={() => handleViewDetails(product)}
            title="Ver detalles"
          >
            <Eye className="h-4 w-4 text-muted-foreground hover:text-foreground" />
          </Button>

          <DropdownMenu modal={false}>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" size="icon">
                <MoreHorizontal className="h-4 w-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuItem onClick={() => handleEdit(product)}>
                <Pencil className="mr-2 h-4 w-4" />
                Editar
              </DropdownMenuItem>
              <DropdownMenuItem onClick={() => handleStock(product)}>
              <PackageIcon className="mr-2 h-4 w-4" />
              Actualizar Stock
            </DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem
              onClick={() => setDeleteProduct(product)}
              className="text-destructive focus:text-destructive"
            >
              <Trash2 className="mr-2 h-4 w-4" />
              Eliminar
            </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      ),
      className: 'w-24', // Increased width for both buttons
    },
  ];

  return (
    <div className="min-h-screen">
      <Header title="Productos" description="Gestiona el catálogo de productos" />

      <div className="p-6 space-y-6">
        {/* Filters */}
        <ProductFilters
          categories={categories}
          onSearch={handleSearch}
          onCategoryFilter={handleCategoryFilter}
          onPriceFilter={handlePriceFilter}
        />

        {/* Actions Bar */}
        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm text-muted-foreground">
              {displayProducts.length} productos
            </p>
          </div>
          <Button onClick={handleCreate}>
            <Plus className="mr-2 h-4 w-4" />
            Nuevo Producto
          </Button>
        </div>

        {/* Table */}
        <DataTable
          columns={columns}
          data={displayProducts}
          keyExtractor={(p) => p.id}
          isLoading={isLoading}
          emptyMessage="No hay productos. Crea uno para comenzar."
        />

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="flex items-center justify-center gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPage((p) => Math.max(0, p - 1))}
              disabled={page === 0}
            >
              <ChevronLeft className="h-4 w-4" />
              Anterior
            </Button>
            <span className="text-sm text-muted-foreground">
              Página {page + 1} de {totalPages}
            </span>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
              disabled={page >= totalPages - 1}
            >
              Siguiente
              <ChevronRight className="h-4 w-4" />
            </Button>
          </div>
        )}
      </div>

      {/* Form Dialog */}
      <ProductForm
        open={isFormOpen}
        onOpenChange={setIsFormOpen}
        product={editProduct}
        categories={categories}
        onSubmit={handleSubmit}
        isLoading={createMutation.isPending || updateMutation.isPending || editLoading}
      />

      {/* Details Dialog */}
      <ProductDetailsDialog
        open={isDetailsOpen}
        onOpenChange={setIsDetailsOpen}
        product={viewProduct}
        isLoading={viewLoading}
      />

      {/* Stock Dialog */}
      <StockDialog
        open={isStockOpen}
        onOpenChange={setIsStockOpen}
        product={selectedProduct}
        onSubmit={handleStockSubmit}
        isLoading={stockMutation.isPending}
      />

      {/* Delete Confirmation */}
      <AlertDialog open={!!deleteProduct} onOpenChange={() => setDeleteProduct(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>¿Eliminar producto?</AlertDialogTitle>
            <AlertDialogDescription>
              El producto "{deleteProduct?.title}" será marcado como eliminado
              (soft delete) y no aparecerá en las listas.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancelar</AlertDialogCancel>
            <AlertDialogAction
              onClick={handleDelete}
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
            >
              {deleteMutation.isPending ? 'Eliminando...' : 'Eliminar'}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
