package edu.skku.cs.skkedula.fragments.bookmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.skku.cs.skkedula.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        var items = arrayListOf<Bookmark>()
        items.add(Bookmark("1", "2"))
        items.add(Bookmark("1", "2"))
        items.add(Bookmark("1", "2"))

        val listAdapter = BookmarkAdapter(requireContext(), items)
        val mainList = binding.scrollview
        mainList.adapter = listAdapter

        val rootView = binding.root
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}