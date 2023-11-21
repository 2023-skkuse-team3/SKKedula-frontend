package edu.skku.cs.skkedula.fragments.bookmark

import androidx.lifecycle.ViewModel

class BookmarkViewModel: ViewModel() {
    var items = arrayListOf<Bookmark>()
    var state = 0
}