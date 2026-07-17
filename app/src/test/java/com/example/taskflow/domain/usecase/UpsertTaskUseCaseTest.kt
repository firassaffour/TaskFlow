package com.example.taskflow.domain.usecase

import com.example.taskflow.data.repository.FakeTaskRepository
import com.example.taskflow.domain.models.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpsertTaskUseCaseTest {

 private lateinit var upsertTaskUseCase: UpsertTaskUseCase
 private lateinit var fakeTaskRepository: FakeTaskRepository

 @Before
 fun setUp() {
  fakeTaskRepository = FakeTaskRepository()
  upsertTaskUseCase = UpsertTaskUseCase(fakeTaskRepository)

 }

 @Test
 fun `Task title cannot be blank`() {
  val task = Task(id = 1, title = "study", description = "test", dueDate = LocalDate.now())
  runBlocking {
   upsertTaskUseCase(task)
   val tasks = fakeTaskRepository.getTasks().first()
   assertEquals(1, tasks.first().id)
  }
 }
}