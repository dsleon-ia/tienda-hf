import { Header } from '@/components/layout/Header';
import { StatCard } from '@/components/ui/stat-card';
import { DataTable, Column } from '@/components/ui/data-table';
import { BadgeStatus } from '@/components/ui/badge-status';
import { useProducts } from '@/hooks/use-products';
import { useCategories } from '@/hooks/use-categories';
import { useLatestAudit } from '@/hooks/use-audit';
import { Package, Tags, Activity, DollarSign, ArrowRight } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Link } from 'react-router-dom';
import { ProductName } from '@/components/audit/ProductName';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';
import { Product, AuditLog } from '@/types';

const actionLabels: Record<string, string> = {
  CREATE: 'Creación',
  UPDATE: 'Actualización',
  DELETE: 'Eliminación',
  STOCK_UPDATE: 'Stock',
};

const actionStatus: Record<string, 'success' | 'warning' | 'error' | 'info'> = {
  CREATE: 'success',
  UPDATE: 'info',
  DELETE: 'error',
  STOCK_UPDATE: 'warning',
};

export default function Dashboard() {
  const { data: productsData, isLoading: productsLoading } = useProducts({ size: 5 });
  const { data: categories, isLoading: categoriesLoading } = useCategories();
  const { data: auditLogs, isLoading: auditLoading } = useLatestAudit();

  const products = productsData?.content || [];
  const totalProducts = productsData?.totalElements || 0;

  const productColumns: Column<Product>[] = [
    {
      key: 'title',
      header: 'Producto',
      cell: (product) => (
        <div className="flex items-center gap-3">
          <div className="h-10 w-10 rounded-lg bg-secondary flex items-center justify-center overflow-hidden">
            {product.image ? (
              <img src={product.image} alt={product.title} className="h-full w-full object-cover" />
            ) : (
              <Package className="h-5 w-5 text-muted-foreground" />
            )}
          </div>
          <div>
            <p className="font-medium text-foreground">{product.title}</p>
            <p className="text-xs text-muted-foreground">{product.category?.name}</p>
          </div>
        </div>
      ),
    },
    {
      key: 'price',
      header: 'Precio',
      cell: (product) => (
        <span className="font-medium text-foreground">${product.price.toFixed(2)}</span>
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
  ];

  const auditColumns: Column<AuditLog>[] = [
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
      cell: (log) => <ProductName productId={log.productId} details={log.details} />,
    },
    {
      key: 'timestamp',
      header: 'Fecha',
      cell: (log) => (
        <span className="text-sm text-muted-foreground">
          {format(new Date(log.timestamp), "d MMM, HH:mm", { locale: es })}
        </span>
      ),
    },
  ];

  return (
    <div className="min-h-screen">
      <Header title="Dashboard" description="Vista general de tu tienda" />
      
      <div className="p-6 space-y-6">
        {/* Stats Grid */}
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
          <StatCard
            title="Total Productos"
            value={totalProducts}
            icon={<Package className="h-6 w-6" />}
          />
          <StatCard
            title="Categorías"
            value={categories?.length || 0}
            icon={<Tags className="h-6 w-6" />}
          />
          <StatCard
            title="Total de Operaciones"
            value={auditLogs?.length || 0}
            icon={<Activity className="h-6 w-6" />}
          />
          <StatCard
            title="Valor Inventario"
            value={`$${products.reduce((acc, p) => acc + p.price * p.stock, 0).toFixed(0)}`}
            icon={<DollarSign className="h-6 w-6" />}
          />
        </div>

        {/* Tables Grid */}
        <div className="grid gap-6 lg:grid-cols-2">
          {/* Recent Products */}
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-foreground">Productos Recientes</h2>
              <Button variant="ghost" size="sm" asChild>
                <Link to="/products" className="flex items-center gap-1">
                  Ver todos <ArrowRight className="h-4 w-4" />
                </Link>
              </Button>
            </div>
            <DataTable
              columns={productColumns}
              data={products}
              keyExtractor={(p) => p.id}
              isLoading={productsLoading}
              emptyMessage="No hay productos"
            />
          </div>

          {/* Recent Audit Logs */}
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-foreground">Actividad Reciente</h2>
              <Button variant="ghost" size="sm" asChild>
                <Link to="/audit" className="flex items-center gap-1">
                  Ver todo <ArrowRight className="h-4 w-4" />
                </Link>
              </Button>
            </div>
            <DataTable
              columns={auditColumns}
              data={(auditLogs || []).slice(0, 5)}
              keyExtractor={(log) => log.id}
              isLoading={auditLoading}
              emptyMessage="Sin actividad reciente"
            />
          </div>
        </div>
      </div>
    </div>
  );
}
