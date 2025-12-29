import { useState } from 'react';
import { Header } from '@/components/layout/Header';
import { DataTable, Column } from '@/components/ui/data-table';
import { BadgeStatus } from '@/components/ui/badge-status';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { useLatestAudit, useAuditByAction, useAuditByProduct } from '@/hooks/use-audit';
import { useSearchProducts } from '@/hooks/use-products';
import { ProductName } from '@/components/audit/ProductName';
import { AuditLog } from '@/types';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';
import { Search, Activity, FileText, Loader2 } from 'lucide-react';

const actionLabels: Record<string, string> = {
  CREATE: 'Creación',
  UPDATE: 'Actualización',
  DELETE: 'Eliminación',
  STOCK_UPDATE: 'Actualización de Stock',
};

const actionStatus: Record<string, 'success' | 'warning' | 'error' | 'info'> = {
  CREATE: 'success',
  UPDATE: 'info',
  DELETE: 'error',
  STOCK_UPDATE: 'warning',
};

export default function Audit() {
  const [selectedAction, setSelectedAction] = useState<string>('all');
  const [productSearchQuery, setProductSearchQuery] = useState('');
  const [selectedProductId, setSelectedProductId] = useState<string | null>(null);

  // Queries
  const { data: latestLogs, isLoading: latestLoading } = useLatestAudit();
  const { data: actionLogs, isLoading: actionLoading } = useAuditByAction(
    selectedAction !== 'all' ? selectedAction : ''
  );
  const { data: productLogs, isLoading: productLoading } = useAuditByProduct(
    selectedProductId || ''
  );
  
  // Product Search for filter
  const { data: searchResults, isLoading: searchLoading } = useSearchProducts(productSearchQuery);
  const showSearchResults = productSearchQuery.length >= 2 && !selectedProductId;

  // Determine which data to display
  let displayLogs: AuditLog[] = [];
  let isLoading = latestLoading;

  if (selectedProductId) {
    displayLogs = productLogs || [];
    isLoading = productLoading;
  } else if (selectedAction !== 'all') {
    displayLogs = actionLogs || [];
    isLoading = actionLoading;
  } else {
    displayLogs = latestLogs || [];
  }

  // Client-side text filtering for deleted products or non-selected search
  if (!selectedProductId && productSearchQuery) {
    const query = productSearchQuery.toLowerCase();
    displayLogs = displayLogs.filter(log => {
      const details = log.details as any;
      const historicalTitle = details?.title || details?.name || details?.productTitle || '';
      return String(historicalTitle).toLowerCase().includes(query) || log.productId.toLowerCase().includes(query);
    });
  }

  const handleProductSelect = (id: string, title: string) => {
    setSelectedProductId(id);
    setProductSearchQuery(title);
    setSelectedAction('all');
  };

  const handleActionChange = (value: string) => {
    setSelectedAction(value);
    setSelectedProductId(null);
    setProductSearchQuery('');
  };

  const handleClearFilters = () => {
    setSelectedAction('all');
    setSelectedProductId(null);
    setProductSearchQuery('');
  };

  const columns: Column<AuditLog>[] = [
    {
      key: 'action',
      header: 'Acción',
      cell: (log) => (
        <BadgeStatus status={actionStatus[log.action] || 'default'}>
          {actionLabels[log.action] || log.action}
        </BadgeStatus>
      ),
    },
    {
      key: 'productId',
      header: 'Producto',
      cell: (log) => (
        <ProductName productId={log.productId} details={log.details} />
      ),
      className: 'w-1/3 min-w-[300px]', // Ancho generoso para el nombre del producto
    },
    {
      key: 'timestamp',
      header: 'Fecha y Hora',
      cell: (log) => (
        <div className="text-sm whitespace-nowrap">
          <p className="text-foreground">
            {format(new Date(log.timestamp), "d 'de' MMMM, yyyy", { locale: es })}
          </p>
          <p className="text-muted-foreground">
            {format(new Date(log.timestamp), 'HH:mm:ss')}
          </p>
        </div>
      ),
      className: 'w-48', // Ancho fijo suficiente para fecha y hora
    },
    {
      key: 'details',
      header: 'Detalles',
      cell: (log) => (
        <div className="w-full">
          {log.details ? (
            <pre className="text-xs text-muted-foreground bg-secondary/50 p-2 rounded overflow-x-auto w-full">
              {JSON.stringify(log.details, null, 2)}
            </pre>
          ) : (
            <span className="text-muted-foreground text-sm">Sin detalles</span>
          )}
        </div>
      ),
      className: 'w-full min-w-[300px]', // Ocupa todo el espacio restante
    },
  ];

  return (
    <div className="min-h-screen">
      <Header
        title="Auditoría"
        description="Historial de operaciones sobre productos"
      />

      <div className="p-6 space-y-6">
        {/* Filters */}
        <div className="glass-card p-4">
          <div className="flex items-center gap-2 mb-4 text-sm font-medium text-foreground">
            <Activity className="h-4 w-4" />
            Filtros de Auditoría
          </div>
          
          <div className="grid gap-4 md:grid-cols-3">
            {/* Search by Product Name */}
            <div className="flex gap-2 relative z-20">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <Input
                  placeholder="Buscar producto..."
                  value={productSearchQuery}
                  onChange={(e) => {
                    setProductSearchQuery(e.target.value);
                    if (e.target.value === '') setSelectedProductId(null);
                  }}
                  className="bg-secondary/50 pl-9"
                />
                
                {/* Search Results Dropdown */}
                {showSearchResults && (
                  <div className="absolute top-full left-0 right-0 mt-1 bg-popover border border-border rounded-md shadow-md max-h-60 overflow-y-auto">
                    {searchLoading ? (
                      <div className="p-2 text-center text-sm text-muted-foreground">
                        <Loader2 className="h-4 w-4 animate-spin inline mr-2" />
                        Buscando...
                      </div>
                    ) : searchResults?.content.length === 0 ? (
                      <div className="p-2 text-center text-sm text-muted-foreground">
                        No se encontraron productos
                      </div>
                    ) : (
                      searchResults?.content.map((product) => (
                        <div
                          key={product.id}
                          className="p-2 hover:bg-accent hover:text-accent-foreground cursor-pointer text-sm"
                          onClick={() => handleProductSelect(product.id, product.title)}
                        >
                          {product.title}
                        </div>
                      ))
                    )}
                  </div>
                )}
              </div>
            </div>

            {/* Filter by Action */}
            <Select value={selectedAction} onValueChange={handleActionChange}>
              <SelectTrigger className="bg-secondary/50">
                <SelectValue placeholder="Tipo de acción" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Todas las acciones</SelectItem>
                <SelectItem value="CREATE">Creación</SelectItem>
                <SelectItem value="UPDATE">Actualización</SelectItem>
                <SelectItem value="DELETE">Eliminación</SelectItem>
                <SelectItem value="STOCK_UPDATE">Actualización de Stock</SelectItem>
              </SelectContent>
            </Select>

            {/* Clear Filters */}
            {(selectedProductId || selectedAction !== 'all' || productSearchQuery) && (
              <Button onClick={handleClearFilters} variant="outline" className="hover:border-destructive hover:text-destructive hover:bg-destructive/10 transition-colors">
                Limpiar filtros
              </Button>
            )}
          </div>
        </div>

        {/* Results Info */}
        <div className="flex items-center gap-2">
          <FileText className="h-4 w-4 text-muted-foreground" />
          <p className="text-sm text-muted-foreground">
            {displayLogs.length} registros
            {selectedProductId && ` para este producto`}
            {selectedAction !== 'all' && ` de tipo "${actionLabels[selectedAction]}"`}
          </p>
        </div>

        {/* Table */}
        <DataTable
          columns={columns}
          data={displayLogs}
          keyExtractor={(log) => log.id}
          isLoading={isLoading}
          emptyMessage="No hay registros de auditoría"
        />
      </div>
    </div>
  );
}
