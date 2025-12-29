import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { Menu } from 'lucide-react';
import { Sidebar } from './Sidebar';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';

export function MainLayout() {
  const [sidebarOpen, setSidebarOpen] = useState(true);

  return (
    <div className="min-h-screen bg-background">
      {/* Header with toggle */}
      <header className={cn(
        "sticky top-0 z-30 flex h-14 items-center gap-4 border-b border-border bg-background/80 px-4 backdrop-blur-xl transition-all duration-300",
        sidebarOpen ? "lg:pl-[calc(16rem+1rem)]" : "lg:pl-4"
      )}>
        <Button
          variant="ghost"
          size="icon"
          onClick={() => setSidebarOpen(!sidebarOpen)}
        >
          <Menu className="h-5 w-5" />
        </Button>
        <span className={cn("font-semibold", sidebarOpen && "lg:hidden")}>Tienda HF</span>
      </header>

      <Sidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />
      
      <main className={cn(
        "min-h-[calc(100vh-3.5rem)] transition-all duration-300",
        sidebarOpen ? "lg:ml-64" : "lg:ml-0"
      )}>
        <Outlet />
      </main>
    </div>
  );
}
