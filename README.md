# AgileProcessChallenge

Este é o projeto desenvolvido como prova técnica para a posição de Android Developer.
Foi desenvolvido um projeto que busca cervejas através da Punk API, exibe-as em uma lista (recyclerview), mostra detalhes da cerveja em uma segunda activity, além da possibilidade de adicionar cervejas para os favoritos.
O projeto foi compilado com a versão 28 do SDK do Android (Android 9.0, versão mais recente). O SDK mínimo é o 23 (Android 6.0).

Devido a simplicidade do exercício e o número razoavelmente baixo de funções nas activities, não foi utilizada nenhuma arquitetura clara(como MVVM, MVP), apenas o padrão Model/Activity. O código fonte está dividido em 5 pacotes:
* data: Para banco de dados e ContentProvider das cervejas favoritas;
* model: Classes objetos (Beer e subclasses tais como Ingredients, Malt, etc);
* retrofit: Inicializador do retrofit e o serviço com as chamadas para a API;
* util: Possui apenas uma classe com constantes, para evitar hardcode;
* view: Contém as activities, adapters para as listas e um componente visual que foi criado.

O app desenvolvido possui 3 activities: uma Splash (SplashActivity), uma que busca e exibe a lista de cervejas (BeerListActivity) e outra que mostra os detalhes e adiciona aos favoritos (BeerDetailsActivity).
* SplashActivity: exibe apenas uma animação com o logo da Agile Process durante 2 segundos e passa para a BeerListActivity;
* BeerListActivity: busca a lista de cervejas na Punk API ou na base de dados de cervejas favoritas. Aqui o usuário pode alterar o modo de busca de cervejas através do menu superior direito. Quando o modo é alterado, é salvo no arquivo de preferências, para que na próxima vez que o app for iniciado, carr


