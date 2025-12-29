import { useState } from 'react';
import { Header } from '@/components/layout/Header';
import { DataTable, Column } from '@/components/ui/data-table';
import { Button } from '@/components/ui/button';
import { CategoryForm } from '@/components/categories/CategoryForm';
import { CategoryProductCount } from '@/components/categories/CategoryProductCount';
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
  useCategories,
  useCreateCategory,
  useUpdateCategory,
  useDeleteCategory,
} from '@/hooks/use-categories';
import { Category } from '@/types';
import { Plus, Pencil, Trash2, Tags } from 'lucide-react';

export default function Categories() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState<Category | null>(null);
  const [deleteCategory, setDeleteCategory] = useState<Category | null>(null);

  const { data: categories, isLoading } = useCategories();
  const createMutation = useCreateCategory();
  const updateMutation = useUpdateCategory();
  const deleteMutation = useDeleteCategory();

  const handleCreate = () => {
    setSelectedCategory(null);
    setIsFormOpen(true);
  };

  const handleEdit = (category: Category) => {
    setSelectedCategory(category);
    setIsFormOpen(true);
  };

  const handleSubmit = (data: { name: string }) => {
    if (selectedCategory) {
      updateMutation.mutate(
        { id: selectedCategory.id, data },
        { onSuccess: () => setIsFormOpen(false) }
      );
    } else {
      createMutation.mutate(data, { onSuccess: () => setIsFormOpen(false) });
    }
  };

  const handleDelete = () => {
    if (deleteCategory) {
      deleteMutation.mutate(deleteCategory.id, {
        onSuccess: () => setDeleteCategory(null),
      });
    }
  };

  const columns: Column<Category>[] = [
    {
      key: 'name',
      header: 'Nombre',
      cell: (category) => (
        <div className="flex items-center gap-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary/10">
            <Tags className="h-5 w-5 text-primary" />
          </div>
          <span className="font-medium text-foreground">{category.name}</span>
        </div>
      ),
    },
    {
      key: 'products',
      header: 'Productos',
      cell: (category) => (
        <CategoryProductCount categoryId={category.id} />
      ),
    },
    {
      key: 'actions',
      header: 'Acciones',
      cell: (category) => (
        <div className="flex items-center gap-2">
          <Button
            variant="ghost"
            size="icon"
            onClick={(e) => {
              e.stopPropagation();
              handleEdit(category);
            }}
          >
            <Pencil className="h-4 w-4" />
          </Button>
          <Button
            variant="ghost"
            size="icon"
            onClick={(e) => {
              e.stopPropagation();
              setDeleteCategory(category);
            }}
            className="text-destructive hover:text-destructive"
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        </div>
      ),
      className: 'w-28',
    },
  ];

  return (
    <div className="min-h-screen">
      <Header
        title="Categorías"
        description="Gestiona las categorías de productos"
      />

      <div className="p-6 space-y-6">
        {/* Actions Bar */}
        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm text-muted-foreground">
              {categories?.length || 0} categorías en total
            </p>
          </div>
          <Button onClick={handleCreate}>
            <Plus className="mr-2 h-4 w-4" />
            Nueva Categoría
          </Button>
        </div>

        {/* Table */}
        <DataTable
          columns={columns}
          data={categories || []}
          keyExtractor={(c) => c.id}
          isLoading={isLoading}
          emptyMessage="No hay categorías. Crea una para comenzar."
        />
      </div>

      {/* Form Dialog */}
      <CategoryForm
        open={isFormOpen}
        onOpenChange={setIsFormOpen}
        category={selectedCategory}
        onSubmit={handleSubmit}
        isLoading={createMutation.isPending || updateMutation.isPending}
      />

      {/* Delete Confirmation */}
      <AlertDialog
        open={!!deleteCategory}
        onOpenChange={() => setDeleteCategory(null)}
      >
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>¿Eliminar categoría?</AlertDialogTitle>
            <AlertDialogDescription>
              Esta acción no se puede deshacer. La categoría "{deleteCategory?.name}"
              solo se puede eliminar si no tiene productos asociados.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancelar</AlertDialogCancel>
            <AlertDialogAction
              onClick={(e) => {
                e.preventDefault();
                handleDelete();
              }}
              disabled={deleteMutation.isPending}
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
