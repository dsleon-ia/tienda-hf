import { useQuery } from '@tanstack/react-query';
import { auditApi } from '@/lib/api';

export function useAuditByProduct(productId: string) {
  return useQuery({
    queryKey: ['audit', 'product', productId],
    queryFn: () => auditApi.getByProductId(productId),
    enabled: !!productId,
  });
}

export function useLatestAudit() {
  return useQuery({
    queryKey: ['audit', 'latest'],
    queryFn: auditApi.getLatest,
  });
}

export function useAuditByAction(action: string) {
  return useQuery({
    queryKey: ['audit', 'action', action],
    queryFn: () => auditApi.getByAction(action),
    enabled: !!action,
  });
}
