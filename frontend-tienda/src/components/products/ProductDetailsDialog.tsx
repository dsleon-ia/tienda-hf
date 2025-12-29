import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog';
import { Product } from '@/types';
import { Loader2, Package, Tag, DollarSign, Archive, Calendar, Trash2 } from 'lucide-react';
import { BadgeStatus } from '@/components/ui/badge-status';

interface ProductDetailsDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  product?: Product | null;
  isLoading?: boolean;
}

export function ProductDetailsDialog({
  open,
  onOpenChange,
  product,
  isLoading,
}: ProductDetailsDialogProps) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Detalles del Producto</DialogTitle>
          <DialogDescription>
            Información completa del producto seleccionado
          </DialogDescription>
        </DialogHeader>

        {isLoading ? (
          <div className="flex justify-center py-8">
            <Loader2 className="h-8 w-8 animate-spin text-primary" />
          </div>
        ) : product ? (
          <div className="space-y-6">
            {/* Header with Image and Title */}
            <div className="flex gap-4">
              <div className="h-24 w-24 flex-shrink-0 rounded-lg bg-secondary flex items-center justify-center overflow-hidden border border-border">
                {product.image ? (
                  <img
                    src={product.image}
                    alt={product.title}
                    className="h-full w-full object-cover"
                  />
                ) : (
                  <Package className="h-10 w-10 text-muted-foreground" />
                )}
              </div>
              <div className="flex-1 space-y-1">
                <h3 className="font-semibold text-lg leading-tight text-foreground">
                  {product.title}
                </h3>
                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                  <Tag className="h-3 w-3" />
                  <span>{product.categoryName || product.category?.name || 'Sin categoría'}</span>
                </div>
                <div className="pt-1">
                  <BadgeStatus
                    status={
                      product.deleted
                        ? 'error'
                        : product.stock > 10
                        ? 'success'
                        : product.stock > 0
                        ? 'warning'
                        : 'error'
                    }
                  >
                    {product.deleted
                      ? 'Eliminado'
                      : product.stock > 0
                      ? 'En Stock'
                      : 'Agotado'}
                  </BadgeStatus>
                </div>
              </div>
            </div>

            {/* Price and Stock Grid */}
            <div className="grid grid-cols-2 gap-4">
              <div className="rounded-lg bg-secondary/50 p-3 space-y-1">
                <span className="text-xs text-muted-foreground flex items-center gap-1">
                  <DollarSign className="h-3 w-3" /> Precio
                </span>
                <p className="text-xl font-bold text-foreground">
                  ${product.price.toFixed(2)}
                </p>
              </div>
              <div className="rounded-lg bg-secondary/50 p-3 space-y-1">
                <span className="text-xs text-muted-foreground flex items-center gap-1">
                  <Archive className="h-3 w-3" /> Stock Disponible
                </span>
                <p className="text-xl font-bold text-foreground">
                  {product.stock}
                </p>
              </div>
            </div>

            {/* Description */}
            <div className="space-y-2">
              <h4 className="text-sm font-medium text-foreground">Descripción</h4>
              <p className="text-sm text-muted-foreground leading-relaxed bg-secondary/20 p-3 rounded-md">
                {product.description}
              </p>
            </div>

            {/* Footer Info */}
            <div className="flex items-center justify-between text-xs text-muted-foreground border-t pt-4">
              {product.deleted && (
                 <span className="text-destructive font-medium flex items-center gap-1">
                   <Trash2 className="h-3 w-3" /> Producto Eliminado
                 </span>
              )}
            </div>
          </div>
        ) : (
          <div className="py-8 text-center text-muted-foreground">
            No se encontró información del producto.
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
}
