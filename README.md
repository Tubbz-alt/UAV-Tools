# UAV-Tools

<p align="center">
  <a href="#">
    <img src="https://img.shields.io/badge/UAV-TOOLS-brightgreen.svg" alt="UAV-Tools">
  </a>
  <a href="https://github.com/jesimar/UAV-Tools/blob/master/LICENSE" target="_blank">
    <img src="https://img.shields.io/aur/license/yaourt.svg" alt="License">
  </a>
  <a href="https://github.com/jesimar/UAV-Tools/pulse" target="_blank">
    <img src="https://img.shields.io/github/downloads/jesimar/UAV-Tools/total.svg" alt="Downloads">
  </a>
</p>

Conjunto de ferramentas desenvolvidas para automatização de voos de Veículos Aéreos Não-Tripulados (VANTs) ou *Unmanned Aerial Vehicles* (UAVs).

## Visão Geral

Nesse projeto podemos encontrar os seguintes diretórios:

* **CreateMapArtificial** -> Código em Java que gera um conjunto de mapas artificiais para os problemas de Planejamento/Replanejamento de Rotas de VANTs. [[CreateMapArtificial](./CreateMapArtificial/)]
* **Libs** -> Bibliotecas utilizadas nos projetos aqui descritos. [[Libs](./Libs/)]
* **ManagerSITL** -> Software em Java que auxilia na automatização de simulações Software-in-the-Loop (SILT) utilizando o ArduPilot SITL. [[ManagerSITL](./ManagerSITL/)]
* **Missions-Ardupilot-SITL** -> Agrupa um conjunto de missões para serem simuladas no ardupilot SITL.[[Missions-Ardupilot-SITL](./Missions-Ardupilot-SITL/)]
* **Modules-IFA** -> Agrupa um conjunto de algoritmos (projetos) utilizados pelo sistema IFA. [[Modules-IFA](./Modules-IFA/)]
* **Modules-MOSA** -> Agrupa um conjunto de algoritmos (projetos) utilizados pelo sistema MOSA. [[Modules-MOSA](./Modules-MOSA/)]
* **ProcessDataFG** -> Software em Java que extrai informações através de imagens de múltiplas execuções do AutoFG. [[ProcessDataFG](./ProcessDataFG/)]
* **Simulations-AutoFG-Waypoints** -> Sistema de piloto automático desenvolvido por Marcelo Hossomi. [[Simulations-AutoFG-Waypoints](./Simulations-AutoFG-Waypoints/)]
* **UAV-Google-Maps** -> Sistema desenvolvido para plotar informações utilizando o Google Maps (utiliza a biblioteca do Márcio Arantes). [[UAV-Google-Maps](./UAV-Google-Maps/)]
* **UAV-Monitoring** -> Aplicação que faz o monitoramento dos sensores e informações da aeronave. [[UAV-Monitoring](./UAV-Monitoring/)]
* **UAV-PosAnalyser** -> Aplicação que faz o monitoramento da posição da aeronave. [[UAV-PosAnalyser](./UAV-PosAnalyser/)]
* **UAV-Map-Converter** -> Sistema desenvolvido para converter os mapas em formato .SGL para os formatos .JSON e .XML. [[UAV-Map-Converter](./UAV-Map-Converter/)]
* **UAV-Plot-Mission** -> Sistema para plotar o mapa da missão e sua rota. [[UAV-Plot-Mission](./UAV-Plot-Mission/)]
* **UAV-Route3DToGeo** -> Projeto em Java que converte uma rota em coordenadas cartesianas para coordenadas geográficas. [[UAV-Route3DToGeo](./UAV-Route3DToGeo/)]
* **UAV-Routes-Standard** -> Código em C que gera um conjunto de rotas com formato padrão (círculo, triângulo e retângulo). [[UAV-Routes-Standard](./UAV-Routes-Standard/)]
* **UAV-Toolkit-C** -> Conjunto de códigos em C para gerenciamento do drone. [[UAV-Toolkit-C](./UAV-Toolkit-C/)]
* **Util-Spreadsheets** -> Conjunto de planilhas para diversos utilidades. [[Util-Spreadsheets](./Util-Spreadsheets/)]

## Contribuidores

Os principais contribuidores deste projeto podem ser encontrados [aqui](https://github.com/jesimar/UAV-Tools/blob/master/AUTHORS)

## Como Contribuir

Se você tem interesse em fazer uma contribuição ao projeto acesse [aqui](https://github.com/jesimar/UAV-Tools/blob/master/CONTRIBUTING.md)

## Changelog

As principais modificações do sistema podem ser acessadas [aqui](https://github.com/jesimar/UAV-Tools/blob/master/CHANGELOG.md). 

## Licença

UAV-Tools está disponível sobre código-fonte aberto com permissões [GNU General Public License v3.0](https://github.com/jesimar/UAV-Tools/blob/master/LICENSE). 

## Agradecimentos

Os autores desse projeto agradecem a Fundação de Amparo à Pesquisa do Estado de São Paulo (FAPESP), número do projeto 2015/23182-2, e a Coordenação de Aperfeiçoamento de Pessoal de Nível Superior (CAPES) pelo apoio financeiro. 

"As opiniões, hipóteses e conclusões ou recomendações expressas neste material são de responsabilidade do(s) autor(es) e não necessariamente refletem a visão da FAPESP"

## Responsabilidade

Este projeto de software não se responsabiliza por eventuais problemas/falhas que possam vir a ocorrer. 
