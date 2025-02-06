// 1) estruturar a definição de produto = criar a
// data class Produto estruturar o estoque de produtos
// = class EstoqueDeProdutos

// 2) construir o menu (visualmente)

// 3) funções de input de dados - produtos (int, double, string)

// 4) lógica de execução de ações do menu

data class Produto(
    val id: Int,
    val nome: String,
    val preco: Double,
    val quantidade: Int
)

interface Estoque<T> {
    fun inserir(item: T)
    fun deletar(id: Int): Boolean
    fun atualizar(item: T): Boolean
    fun buscar(id: Int): T?
    fun buscarTodos(): List<T>
}

class EstoqueDeProdutos : Estoque<Produto> {

    private var listaProdutos = mutableListOf<Produto>()

    override fun inserir(item: Produto) {
        listaProdutos.add(item)
    }

    override fun deletar(id: Int): Boolean {
        return listaProdutos.removeIf { it.id == id }
    }

    override fun buscar(id: Int): Produto? {
        return listaProdutos.find { it.id == id }
    }

    override fun buscarTodos(): List<Produto> {
        return listaProdutos.toList()
    }

    override fun atualizar(item: Produto): Boolean {
        if (listaProdutos.removeIf { it.id == item.id }) {
            listaProdutos.add(item)
            return true
        } else {
            return false
        }
    }
}

fun preencherProdutoAtualizado(produtoASerAtualizado: Produto): Produto {

    var nome: String? = null
    println("Altera o NOME do produto (caso não queira mudar, tecle ENTER):")
    while (nome.isNullOrEmpty()) {
        print("-> ")
        nome = readlnOrNull()?.ifEmpty { produtoASerAtualizado.nome }
        if (nome.isNullOrEmpty()) {
            println("O NOME inserido é inválido. Tente novamente.")
        }
    }

    var preco: Double? = null
    println("Altere o PREÇO do produto (caso não queira mudar, tecle ENTER):")
    while (preco == null || preco < 0) {
        print("-> R$ ")
        preco = readlnOrNull()?.ifEmpty { produtoASerAtualizado.preco.toString() }?.toDoubleOrNull()
        if (preco == null || preco < 0) {
            println("O PREÇO inserido é inválido. Tente novamente.")
        }
    }

    var quantidade: Int? = null
    println("Altere a QUANTIDADE do produto em estoque (caso não queira mudar, tecle ENTER):")
    while (quantidade == null || quantidade < 0) {
        print("-> ")
        quantidade = readlnOrNull()?.ifEmpty { produtoASerAtualizado.quantidade.toString() }?.toIntOrNull()
        if (quantidade == null || quantidade < 0) {
            println("A QUANTIDADE inserida é inválida. Tente novamente.")
        }
    }

    return Produto(
        id = produtoASerAtualizado.id,
        nome = nome,
        preco = preco,
        quantidade = quantidade
    )
}

fun preencherProduto(): Produto {

    var id: Int? = null
    println("Insira o ID do produto:")
    while (id == null || id < 0) {
        print("-> ")
        id = readlnOrNull()?.toIntOrNull()
        if (id == null || id < 0) {
            println("O ID inserido é inválido. Tente novamente!")
        }
    }

    var nome: String? = null
    println("Insira o NOME do produto:")
    while (nome.isNullOrEmpty()) {
        print("-> ")
        nome = readlnOrNull()
        if (nome.isNullOrEmpty()) {
            println("O NOME inserido é inválido. Tente novamente.")
        }
    }

    var preco: Double? = null
    println("Insira o PREÇO do produto:")
    while (preco == null || preco < 0) {
        print("-> R$ ")
        preco = readlnOrNull()?.toDoubleOrNull()
        if (preco == null || preco < 0) {
            println("O PREÇO inserido é inválido. Tente novamente.")
        }
    }

    var quantidade: Int? = null
    println("Insira a QUANTIDADE do produto em estoque:")
    while (quantidade == null || quantidade < 0) {
        print("-> ")
        quantidade = readlnOrNull()?.toIntOrNull()
        if (quantidade == null || quantidade < 0) {
            println("A QUANTIDADE inserida é inválida. Tente novamente.")
        }
    }

    return Produto(
        id = id,
        nome = nome,
        preco = preco,
        quantidade = quantidade
    )
}

fun main() {

    val estoqueDeProdutos = EstoqueDeProdutos()

    var acao: Int? = null

    while (acao != 5) {
        println(
            """
        +---------------------------------+
        |  CONTROLE DE ESTOQUE - PRODUTOS |
        +---------------------------------+
        |  1 - Adicionar                  |
        |  2 - Atualizar                  |
        |  3 - Deletar                    |
        |  4 - Buscar                     |
        |  5 - Sair                       |
        +---------------------------------+
        """
        )
        println("LISTA ATUAL DE PRODUTOS EM ESTOQUE:")
        println(estoqueDeProdutos.buscarTodos().joinToString(
            separator = "\n"
        ).ifEmpty { "Nenhum produto foi adicionado ao estoque até o momento." })
        print("-> ")
        acao = readlnOrNull()?.toIntOrNull()

        when (acao) {
            1 -> {
                val produto = preencherProduto()
                estoqueDeProdutos.inserir(item = produto)
                println("Produto inserido com SUCESSO!")
            }

            2 -> {
                var id: Int? = null
                println("Insira o ID do produto a ser atualizado:")
                while (id == null || id < 0) {
                    print("-> ")
                    id = readlnOrNull()?.toIntOrNull()
                    if (id == null || id < 0 || estoqueDeProdutos.buscar(id = id) == null) {
                        println("O ID inserido é inválido. Tente novamente.")
                    }
                }

                val produtoASerAtualizado = estoqueDeProdutos.buscar(id)
                produtoASerAtualizado?.let {
                    val produtoAtualizado = preencherProdutoAtualizado(it)
                    estoqueDeProdutos.atualizar(item = produtoAtualizado)
                    println("Produto atualizado com SUCESSO!")
                }
            }

            3 -> {
                var id: Int? = null
                println("Insira o ID do produto a ser deletado:")
                while (id == null || id < 0) {
                    print("-> ")
                    id = readlnOrNull()?.toIntOrNull()
                    if (id == null || id < 0 || estoqueDeProdutos.buscar(id = id) == null) {
                        println("O ID inserido é inválido. Tente novamente.")
                    }
                }

                estoqueDeProdutos.deletar(id)
                println("Produto deletado com SUCESSO!")
            }

            4 -> {
                var id: Int? = null
                println("Insira o ID do produto a ser buscado:")
                while (id == null || id < 0) {
                    print("-> ")
                    id = readlnOrNull()?.toIntOrNull()
                    if (id == null || id < 0 || estoqueDeProdutos.buscar(id = id) == null) {
                        println("O ID inserido é inválido. Tente novamente.")
                    }
                }

                val produtoBuscado = estoqueDeProdutos.buscar(id)
                println(produtoBuscado)
            }

            5 -> {
                println("Obrigado, volte sempre!")
            }

            else -> {
                println("Opção inválida. Tente novamente!")
            }
        }
    }
}

