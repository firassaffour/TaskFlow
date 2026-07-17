package com.example.taskflow.domain.usecase

import com.example.taskflow.data.repository.FakeTaskRepository
import com.example.taskflow.domain.models.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class GetTasksUseCaseTest {

 private lateinit var getTasksUseCase: GetTasksUseCase
 private lateinit var fakeTaskRepository: FakeTaskRepository

 @Before
 fun setUp() {
  fakeTaskRepository = FakeTaskRepository()
  getTasksUseCase = GetTasksUseCase(fakeTaskRepository)

  val tasksToInsert = mutableListOf<Task>()
  ('a'..'z').forEachIndexed { index, c ->
   tasksToInsert.add(
    Task(
     title = c.toString(),
     description = c.toString(),
     id = index,
     dueDate = LocalDate.now()
    )
   )
  }
  tasksToInsert.shuffle()
  runBlocking {
   tasksToInsert.forEach { fakeTaskRepository.upsertTask(it) }
  }
 }

 @Test
 fun `getTasks UseCase returns tasks`() = runBlocking {
  val tasks = getTasksUseCase().first()
  assertEquals(26, tasks.size)
 }
}