import { useProductsByCategory } from '@/hooks/use-products';
import { Loader2 } from 'lucide-react';

interface CategoryProductCountProps {
  categoryId: string;
}

export function CategoryProductCount({ categoryId }: CategoryProductCountProps) {
  // We only need 1 item to get the total count in the metadata
  const { data, isLoading } = useProductsByCategory(categoryId, 0, 1);

  if (isLoading) {
    return <Loader2 className="h-3 w-3 animate-spin text-muted-foreground" />;
  }

  const count = data?.totalElements || 0;

  return (
    <span className="text-muted-foreground">
      {count} {count === 1 ? 'producto' : 'productos'}
    </span>
  );
}
