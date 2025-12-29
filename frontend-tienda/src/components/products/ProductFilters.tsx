import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Category } from '@/types';
import { Search, X, Filter } from 'lucide-react';

interface ProductFiltersProps {
  categories: Category[];
  onSearch: (query: string) => void;
  onCategoryFilter: (categoryId: string | null) => void;
  onPriceFilter: (min: number | null, max: number | null) => void;
}

export function ProductFilters({
  categories,
  onSearch,
  onCategoryFilter,
  onPriceFilter,
}: ProductFiltersProps) {
  const [searchQuery, setSearchQuery] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');

  const handleSearch = () => {
    onSearch(searchQuery);
  };

  const handleCategoryChange = (value: string) => {
    setSelectedCategory(value);
    onCategoryFilter(value === 'all' ? null : value);
  };

  const handlePriceFilter = () => {
    const min = minPrice ? parseFloat(minPrice) : null;
    const max = maxPrice ? parseFloat(maxPrice) : null;
    onPriceFilter(min, max);
  };

  const handleClearFilters = () => {
    setSearchQuery('');
    setMinPrice('');
    setMaxPrice('');
    setSelectedCategory('all');
    onSearch('');
    onCategoryFilter(null);
    onPriceFilter(null, null);
  };

  const hasFilters = searchQuery || minPrice || maxPrice || selectedCategory !== 'all';

  return (
    <div className="glass-card p-4 space-y-4">
      <div className="flex items-center gap-2 text-sm font-medium text-foreground">
        <Filter className="h-4 w-4" />
        Filtros
      </div>
      
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {/* Search */}
        <div className="relative">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            placeholder="Buscar productos..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
            className="bg-secondary/50 pl-9"
          />
        </div>

        {/* Category */}
        <Select value={selectedCategory} onValueChange={handleCategoryChange}>
          <SelectTrigger className="bg-secondary/50">
            <SelectValue placeholder="Categoría" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">Todas las categorías</SelectItem>
            {categories.map((category) => (
              <SelectItem key={category.id} value={category.id}>
                {category.name}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>

        {/* Price Range */}
        <div className="flex gap-2 items-center">
          <Input
            type="number"
            placeholder="Min $"
            value={minPrice}
            onChange={(e) => setMinPrice(e.target.value)}
            className="bg-secondary/50"
          />
          <span className="text-muted-foreground">-</span>
          <Input
            type="number"
            placeholder="Max $"
            value={maxPrice}
            onChange={(e) => setMaxPrice(e.target.value)}
            className="bg-secondary/50"
          />
        </div>

        {/* Action Buttons */}
        <div className="flex gap-2">
          <Button onClick={handlePriceFilter} variant="secondary" className="flex-1">
            Aplicar
          </Button>
          {hasFilters && (
            <Button onClick={handleClearFilters} variant="ghost" size="icon">
              <X className="h-4 w-4" />
            </Button>
          )}
        </div>
      </div>
    </div>
  );
}
