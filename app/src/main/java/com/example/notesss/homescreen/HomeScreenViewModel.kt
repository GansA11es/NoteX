package com.example.notesss.homescreen

import androidx.lifecycle.*
import com.example.notesss.database.Note
import com.example.notesss.database.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class NoteSortOrder {
    NONE,
    ASC_ID,
    DESC_ID,
    ASC_TITLE,
    DESC_TITLE
}

class HomeScreenNoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val sortOrderLiveData = MutableLiveData(NoteSortOrder.NONE)

    fun setSortOrder(sortOrder: NoteSortOrder) {
        sortOrderLiveData.value = sortOrder
    }

    val notes: LiveData<List<Note>> = sortOrderLiveData.switchMap {
        when (it) {
            NoteSortOrder.NONE -> repository.getAllNote()
            NoteSortOrder.ASC_ID -> repository.sortByNewId()
            NoteSortOrder.DESC_ID -> repository.sortByOldId()
            NoteSortOrder.ASC_TITLE -> repository.sortByTitleAsc()
            NoteSortOrder.DESC_TITLE -> repository.sortByTitleDesc()
        }
    }

    fun removeAllNote() = viewModelScope.launch(Dispatchers.IO) {
        repository.removeAllNote()
    }

    fun searchNote(query: String) = repository.searchNote(query)

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(note)
    }
}
