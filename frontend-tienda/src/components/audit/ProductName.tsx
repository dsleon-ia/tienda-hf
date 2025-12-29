import { useProduct } from '@/hooks/use-products';
import { Loader2, Archive } from 'lucide-react';

interface ProductNameProps {
  productId: string;
  details?: any;
}

export function ProductName({ productId, details }: ProductNameProps) {
  // Intentamos obtener el producto actual
  const { data: product, isLoading, isError } = useProduct(productId);

  // Intentamos extraer el título de los detalles históricos como fallback
  // El backend puede guardar el título en 'title' o 'name' dentro de details
  const historicalTitle = details?.title || details?.name || details?.productTitle;

  if (isLoading) {
    // Si tenemos el título histórico, lo mostramos mientras carga (optimistic UI)
    // O si prefieres solo loading:
    return historicalTitle ? (
      <div className="flex flex-col opacity-70">
        <span className="font-medium text-foreground flex items-center gap-1">
          {historicalTitle} <Loader2 className="h-3 w-3 animate-spin" />
        </span>
        <span className="text-xs text-muted-foreground font-mono">#{productId.slice(0, 8)}</span>
      </div>
    ) : (
      <Loader2 className="h-3 w-3 animate-spin text-muted-foreground" />
    );
  }

  // Si hay error (producto no encontrado/eliminado) o no viene data
  if (isError || !product) {
    if (historicalTitle) {
      return (
        <div className="flex flex-col">
          <span className="font-medium text-muted-foreground flex items-center gap-2" title="Producto eliminado o no disponible">
            {historicalTitle}
            <Archive className="h-3 w-3" />
          </span>
        </div>
      );
    }
    return <span className="text-muted-foreground italic">Desconocido ({productId.slice(0, 8)}...)</span>;
  }

  // Producto encontrado y activo
  return (
    <div className="flex flex-col">
      <span className="font-medium text-foreground">{product.title}</span>
    </div>
  );
}
