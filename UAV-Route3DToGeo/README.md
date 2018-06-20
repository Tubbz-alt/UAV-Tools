# UAV-Route3DToGeo

O projeto UAV-Route3DToGeo foi desenvolvido para auxiliar a projetar missões e mapas usando o [Google Earth](https://www.google.com/earth/index.html). Este projeto foi desenvolvido em Java usando o IDE [Netbeans](https://netbeans.org/).

## Visão Geral
 
Esse projeto conta com um conjunto de ferramentas para conversões de rotas em coordenadas cartesianas em coordenadas geograficas (e vice-versa), conta também com conversões de mapas projetados no Google Earth para mapas em coordenadas cartesianas. Dentre inumeras outras funcionalidades.

Um arquivo de configurações (config-route.properties) encontra-se também disponível para configuração de um conjunto de propriedades importantes.

* **Route3DToGeo** -> Sistema responsável por a partir de um arquivo de coordenadas cartesianas (3D) e um ponto geográfico (arquivo geoBase.txt) fazer uma transformação para o sistema de coordenadas de geográficos (latitude, longitude e altitude).

Os arquivos de entrada/saída desse sistema ficam dentro do diretório: mission

* **mission/3d** -> arquivos usados no Route3DToGeo
