import { Header } from '@/components/layout/Header';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { ExternalLink, Code2, Server, Database, Globe } from 'lucide-react';

export default function Settings() {
  return (
    <div className="min-h-screen">
      <Header title="Configuración" description="Información del sistema y recursos" />

      <div className="p-6 space-y-6">
        {/* API Documentation Section */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Server className="h-5 w-5 text-primary" />
              Documentación de API
            </CardTitle>
            <CardDescription>
              Accede a la documentación interactiva de la API REST del backend.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between bg-secondary/20 p-4 rounded-lg border border-border">
              <div>
                <h4 className="font-medium mb-1">Swagger UI</h4>
                <p className="text-sm text-muted-foreground">
                  Explora y prueba los endpoints de la API directamente desde tu navegador.
                </p>
              </div>
              <Button asChild className="shrink-0">
                <a 
                  href="http://localhost:8081/swagger-ui/index.html?urls.primaryName=api#/" 
                  target="_blank" 
                  rel="noopener noreferrer"
                  className="gap-2"
                >
                  Abrir Swagger <ExternalLink className="h-4 w-4" />
                </a>
              </Button>
            </div>
          </CardContent>
        </Card>

        {/* Frontend Stack Section */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Code2 className="h-5 w-5 text-primary" />
              Stack Tecnológico - Frontend
            </CardTitle>
            <CardDescription>
              Tecnologías y librerías utilizadas en la construcción de esta interfaz.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <StackItem 
                icon={<Globe className="h-5 w-5 text-blue-500" />}
                title="React 18"
                description="Biblioteca principal de UI"
              />
              <StackItem 
                icon={<Code2 className="h-5 w-5 text-yellow-500" />}
                title="Vite"
                description="Build tool y servidor de desarrollo"
              />
              <StackItem 
                icon={<Code2 className="h-5 w-5 text-sky-500" />}
                title="Tailwind CSS"
                description="Framework de estilos utility-first"
              />
              <StackItem 
                icon={<Database className="h-5 w-5 text-red-500" />}
                title="TanStack Query"
                description="Gestión de estado asíncrono y cache"
              />
              <StackItem 
                icon={<Code2 className="h-5 w-5 text-orange-500" />}
                title="React Router"
                description="Enrutamiento del lado del cliente"
              />
              <StackItem 
                icon={<img src="https://images.seeklogo.com/logo-png/51/1/shadcn-ui-logo-png_seeklogo-519786.png" alt="Shadcn/ui" className="h-5 w-5 object-contain" />}
                title="Shadcn/ui"
                description="Componentes de UI reutilizables"
              />
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

function StackItem({ icon, title, description }: { icon: React.ReactNode, title: string, description: string }) {
  return (
    <div className="flex items-start gap-3 p-3 rounded-lg bg-secondary/10 border border-border/50 hover:bg-secondary/20 transition-colors">
      <div className="mt-0.5 bg-background p-1.5 rounded-md shadow-sm border border-border">
        {icon}
      </div>
      <div>
        <h4 className="font-medium text-sm text-foreground">{title}</h4>
        <p className="text-xs text-muted-foreground leading-snug mt-0.5">
          {description}
        </p>
      </div>
    </div>
  );
}
