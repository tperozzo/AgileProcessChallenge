# AgileProcessChallenge

##Introdução

Este é o projeto desenvolvido como prova técnica para a posição de Android Developer.
Foi desenvolvido um projeto que busca cervejas através da Punk API, exibe-as em uma lista (recyclerview), mostra detalhes da cerveja em uma segunda activity, além da possibilidade de adicionar cervejas para os favoritos.
O projeto foi compilado com a versão 28 do SDK do Android (Android 9.0, versão mais recente). O SDK mínimo é o 23 (Android 6.0).

##Arquitetura

Devido a simplicidade do exercício e o número razoavelmente baixo de funções nas activities, não foi utilizada nenhuma arquitetura clara(como MVVM, MVP), apenas o padrão Model/Activity. O código fonte está dividido em 5 pacotes:
* data: Para banco de dados e ContentProvider das cervejas favoritas;
* model: Classes objetos (Beer e subclasses tais como Ingredients, Malt, etc);
* retrofit: Inicializador do retrofit e o serviço com as chamadas para a API;
* util: Possui apenas uma classe com constantes, para evitar hardcode;
* view: Contém as activities, adapters para as listas e um componente visual que foi criado.

O app desenvolvido possui 3 activities: uma Splash (SplashActivity), uma que busca e exibe a lista de cervejas (BeerListActivity) e outra que mostra os detalhes e adiciona aos favoritos (BeerDetailsActivity).
* SplashActivity: exibe apenas uma animação com o logo da Agile Process durante 2 segundos e passa para a BeerListActivity;
* BeerListActivity: busca a lista de cervejas na Punk API ou na base de dados de cervejas favoritas. Aqui o usuário pode alterar o modo de busca de cervejas através do menu superior direito. Quando o modo é alterado, é salvo no arquivo de preferências (Shared Preferences), para que na próxima vez que o app for iniciado, carregar a lista conforme o último modo utilizado. Quando o modo de carregamento de cervejas é através da API, o aplicativo permite que o usuário carregue de 10 em 10 cervejas. Toda vez que as cervejas são carregadas assim, a recyclerview é "scrollada" para a posição da primeira nova cerveja na lista. Quando o modo de carregamento é através das favoritas, as cervejas são buscadas no banco de dados local através de um Cursor.
* DetailsListActivity: carrega todas as informações de uma cerveja, tendo ela vindo da lista de favoritas ou da lista da API. Além disso, é nesta activity que a cerveja é adicionada ou removida do banco de favoritas (através das funções do ContentProvider). A cerveja que foi carregada na activity anterior pela API e passada para a activity de detalhes pela intent (como Serializable), enquanto que a cerveja que veio da lista de favoritas é carregada por meio de uma chamada para a API pelo id. Isso foi desenvolvido assim pois o banco de dados não contém todos os dados de uma cerveja e sim apenas algumas informações para criar a recyclerview. Além de que é interessante explorar outra chamada da API.


