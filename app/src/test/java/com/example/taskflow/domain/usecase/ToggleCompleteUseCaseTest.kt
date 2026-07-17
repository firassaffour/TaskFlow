package com.example.taskflow.domain.usecase

import com.example.taskflow.data.repository.FakeTaskRepository
import com.example.taskflow.domain.models.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class ToggleCompleteUseCaseTest {

  private lateinit var toggleCompleteUseCase: ToggleCompleteUseCase
  private lateinit var fakeTaskRepository: FakeTaskRepository

@Before
 fun setUp() {
  fakeTaskRepository = FakeTaskRepository()
  toggleCompleteUseCase = ToggleCompleteUseCase(fakeTaskRepository)
 }

  @Test
  fun `toggleCompleteUseCase toggles task completion`(){
   val task = Task(id = 1, title = "study", description = "test", dueDate = LocalDate.now())

   runBlocking {

    val tasks = fakeTaskRepository.getTasks().first()

    fakeTaskRepository.upsertTask(task)
    toggleCompleteUseCase(task)

    assertEquals(true, tasks.first().isCompleted)
   }
  }
}