import java.util.*


/**
 * CRIAR UMA APLICAÇÃO PARA UMA LISTA DE TAREFAS
 * 1. (x) ESTRUTURAR A DATA CLASS TASK COM AS PROPRIEDADES
 * 2. (x) ESTRUTURAR A CLASSE TASK MANAGER
 * 3. USAR FUNÇÕES COMO REQUIRE PARA GARANTIR QUE O TITULO NÃO ESTEJA VAZIO, A TAREFA EXISTE
 * ANTES DE REALIZAR OPERAÇÕES COMO ATUALIZAR E EXCLUIR.
 * 4. (x) UTILIZAR SEALED CLASS PARA REPRESENTAR RETORNO AS OPERAÇÕES Sucess e Error.
 * 5. ADICIONAR FUNÇÕES DE EXTENSÃO PARA CONVERTER Task em uma String formatada, obter a contagem
 * de tarefas concluídas da lista de tarefas.
 */

data class Task(
    val id: Int,
    val title: String,
    val description: String?,
    var isCompleted: Boolean,
    val createdAt: Date
) {
//    init {
//        require(title.isNotBlank()) {"O título da tarefa não pode estar vazio."}
//    }

    companion object {
        private var currentId = 0

        fun generateId(): Int {
            return ++currentId
        }
    }
}

sealed class TaskResult {
    object Success : TaskResult()
    object Failure : TaskResult()
}

interface Manager<T> {
    fun add(item: T)
    fun listAll(): List<T>
    fun find(id: Int): T?
    fun update(id: Int, isCompleted: Boolean): TaskResult
    fun delete(id: Int): TaskResult
    fun filter(isCompleted: Boolean): List<T>
}

class TaskManager : Manager<Task> {

    private val tasks = mutableListOf<Task>()

    override fun add(item: Task) {
        tasks.add(item)
    }

    override fun listAll(): List<Task> {
        return tasks.toList()
    }

    override fun find(id: Int): Task? {
        return tasks.find { it.id == id }
    }

    override fun update(id: Int, isCompleted: Boolean): TaskResult {
//        requireNotNull(taskToBeUpdated) { "Erro: Nenhuma tarefa encontrada com o ID $id." }
//
//        taskToBeUpdated.isCompleted = isCompleted
//        return true
//         return taskToBeUpdated?.let { task ->
//            task.isCompleted = isCompleted
//            true
//        } ?: false

        val taskToBeUpdated = find(id = id)

        lateinit var result: TaskResult

        if (taskToBeUpdated == null) {
            result = TaskResult.Failure
        } else {
            taskToBeUpdated.isCompleted = isCompleted
            result = TaskResult.Success
        }

        return result
    }

    override fun delete(id: Int): TaskResult {
        val taskRemoved = tasks.removeIf {
            it.id == id
        }

        val result: TaskResult = if (taskRemoved) {
            TaskResult.Success
        } else {
            TaskResult.Failure
        }

        return result
    }

    override fun filter(isCompleted: Boolean): List<Task> {
        val filteredTasks: List<Task> = if (isCompleted) {
            tasks.filter { it.isCompleted }
        } else {
            tasks.filter { !it.isCompleted }
        }
        return filteredTasks
    }
}

fun setTask(): Task {

    println("Digite um TÍTULO para a tarefa:")
    var title: String? = null
    while (title.isNullOrEmpty()) {
        print("-> ")
        title = readlnOrNull()

        if (title.isNullOrEmpty()) {
            println("O TÍTULO inserido é inválido. Tente novamente.")
        }
    }

    println("Digite um DESCRIÇÃO para a tarefa (ou tecle ENTER para continuar):")
    var description: String? = readln().takeIf { it.isNotBlank() }

    return Task(
        id = Task.generateId(),
        title = title,
        description = description,
        isCompleted = false,
        createdAt = Date()
    )
}

fun main() {

    val taskManager = TaskManager()

    var action: Int? = null

    while (action != 7) {
        println(
            """
        +---------------------------------+
        |          TASK MANAGER           |
        +---------------------------------+
        |  1 - Adicionar nova             |
        |  2 - Listar todas               |
        |  3 - Buscar por ID              |
        |  4 - Atualizar status           |
        |  5 - Excluir por ID             |
        |  6 - Filtrar por status         |
        |  7 - Sair                       |
        +---------------------------------+
        """
        )
        println("ESCOLHA UMA OPÇÃO:")
        print("-> ")
        action = readlnOrNull()?.toIntOrNull()

        when (action) {
            1 -> {
                val task = setTask()
                taskManager.add(item = task)
                println("Tarefa criada com SUCESSO!")
            }

            2 -> {
//                println(
//                    taskManager.listAll().joinToString(separator = "\n")
//                        .ifEmpty { "Nenhuma tarefa foi adicionada até o momento." })

                if (taskManager.listAll().none()) {
                    println("Nenhuma tarefa foi adicionada até o momento.")
                } else {
                    taskManager.listAll().forEach { task ->
                        println("Tarefa: ${task.title} - Concluída: ${task.isCompleted}")
                    }
                }
            }

            3 -> {
                println("Digite o ID da tarefa:")
                var id: Int? = null
                while (id == null || id <= 0) {
                    print("-> ")
                    id = readlnOrNull()?.toIntOrNull()
                    if (id == null || id <= 0) {
                        println("ID inválido, por favor tente novamente!")
                    }
                }

                val task = taskManager.find(id)
                if (task == null) {
                    println("Nenhuma tarefa corresponde a esse ID.")
                } else {
                    println(task)
                }
            }

            4 -> {
                println("Digite o ID da tarefa:")
                var id: Int? = null

                while (id == null || id <= 0) {
                    print("-> ")
                    id = readlnOrNull()?.toIntOrNull()
                    if (id == null || id <= 0) {
                        println("ID inválido, por favor tente novamente!")
                    }
                }

                println("Digite TRUE para concluir a tarefa ou FALSE para manter o status a fazer:")
                var isCompleted: Boolean? = null

                while (isCompleted == null) {
                    print("-> ")
                    val input = readlnOrNull()
                    isCompleted = when (input) {
                        "TRUE" -> true
                        "FALSE" -> false
                        else -> null
                    }

                    if (isCompleted == null) {
                        println("Status inválido! Digite TRUE ou FALSE.")
                    }
                }

                val taskUpdated = taskManager.update(id, isCompleted)

                when (taskUpdated) {
                    is TaskResult.Success -> println("Status da tarefa atualizado com SUCESSO!")
                    is TaskResult.Failure -> println("Erro ao atualizar status da tarefa.")
                }

//                val task = taskManager.find(id)
//                if (task == null) {
//                    println("Nenhuma tarefa corresponde a esse ID!")
//                } else {
//                    task.let {
//                        it.isCompleted = !it.isCompleted
//                        println("Status da tarefa atualizado com SUCESSO!")
//                    }
//                }
            }

            5 -> {
                println("Digite o ID da tarefa:")
                var id: Int? = null
                while (id == null || id <= 0) {
                    print("-> ")
                    id = readlnOrNull()?.toIntOrNull()
                    if (id == null || id <= 0) {
                        println("ID inválido, por favor tente novamente!")
                    }
                }

                val taskDeleted = taskManager.delete(id)

                when (taskDeleted) {
                    is TaskResult.Success -> println("Tarefa deletada com SUCESSO!")
                    is TaskResult.Failure -> println("Erro ao deletar tarefa.")
                }
            }

            6 -> {
                println("Digite TRUE para filtrar tarefas concluídas e FALSE para tarefas a fazer:")
                var isCompleted: Boolean? = null

                while (isCompleted == null) {
                    print("-> ")
                    val input = readlnOrNull()
                    isCompleted = when (input) {
                        "TRUE" -> true
                        "FALSE" -> false
                        else -> null
                    }

                    if (isCompleted == null) {
                        println("Status inválido! Digite TRUE ou FALSE.")
                    }
                }

                println(
                    taskManager.filter(isCompleted).joinToString(separator = "\n")
                        .ifEmpty { "Nenhuma tarefa encontrada com esse status." })
            }

            7 -> {
                println("Obrigado, volte sempre!")
            }

            else -> {
                println("Opção inválida. Tente novamente!")
            }
        }
    }
}
