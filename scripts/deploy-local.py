import subprocess
import sys

NAMESPACE = "shotit"

INTEGRATION_MANIFESTS = [
    "shottit",
    "shotit-secrets",
    "shotit-database",
    "shotit-localstack",
    "shotit-queue",
]

APPLICATION_MANIFEST = [ "shotit-api" ]

def k_apply(directory, manifest):
    """
        Aplica um manifesto no cluster kubernetes
    """
    print(f"Aplicando {manifest} no cluster {NAMESPACE}")

    filename = f"../{directory}/{manifest}.yaml"
    try:
        subprocess.run(["kubectl", "--namespace", NAMESPACE, "apply", "-f", filename], check=True)
    except subprocess.CalledProcessError as e:
        print(f"Failed to apply {manifest}: {e}")

def k_port_forward(service, local_port, container_port):
    """
        Faz o port forward do serviço para que possa ser utilizado de maneira local
    """
    print(f"Fazendo port-forward do serviço {service} <local>{local_port}:<container>{container_port}...")
    try:
        subprocess.Popen([
            "kubectl", "--namespace", NAMESPACE, "port-forward", 
            f"svc/{service}", f"{local_port}:{container_port}"
        ])
        print(f"Port-forwarding iniciado para o serviço {service}...")
    except Exception as e:
        print(f"Erro ao executar o port-forward do serviço {service}: {e}")


if __name__ == "__main__":
    k8s_directory = "k8s"
    deployment_type = sys.argv[1]
    
    if deployment_type == 'sidecar':
        
        for manifest in INTEGRATION_MANIFESTS:
            k_apply(k8s_directory, manifest)
        
        k_port_forward("rabbitmq-dashboard", 15672, 15672)
        k_port_forward("rabbitmq", 5672)
        k_port_forward("mysql", 3306, 3306)
        k_port_forward("localstack", 4566, 4566)

        print("Serviços Iniciados!")
    
    elif deployment_type == 'full':
        for manifest in INTEGRATION_MANIFESTS:
            k_apply(k8s_directory, manifest)

        for manifest in APPLICATION_MANIFEST:
            k_apply(k8s_directory, manifest)

        k_port_forward("localstack", 4566, 4566)
        k_port_forward("shotit-api", 8080, 80)

        print("API Iniciada na porta 8080")

