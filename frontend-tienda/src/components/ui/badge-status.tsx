import { cn } from '@/lib/utils';

type StatusType = 'success' | 'warning' | 'error' | 'info' | 'default';

interface BadgeStatusProps {
  status: StatusType;
  children: React.ReactNode;
  className?: string;
}

const statusStyles: Record<StatusType, string> = {
  success: 'bg-success/10 text-success border-success/20',
  warning: 'bg-warning/10 text-warning border-warning/20',
  error: 'bg-destructive/10 text-destructive border-destructive/20',
  info: 'bg-primary/10 text-primary border-primary/20',
  default: 'bg-muted text-muted-foreground border-border',
};

export function BadgeStatus({ status, children, className }: BadgeStatusProps) {
  return (
    <span
      className={cn(
        'inline-flex items-center gap-1.5 rounded-full border px-2.5 py-0.5 text-xs font-medium',
        statusStyles[status],
        className
      )}
    >
      <span className={cn('h-1.5 w-1.5 rounded-full', {
        'bg-success': status === 'success',
        'bg-warning': status === 'warning',
        'bg-destructive': status === 'error',
        'bg-primary': status === 'info',
        'bg-muted-foreground': status === 'default',
      })} />
      {children}
    </span>
  );
}
