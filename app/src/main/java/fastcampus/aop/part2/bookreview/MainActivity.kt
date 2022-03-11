package fastcampus.aop.part2.bookreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fastcampus.aop.part2.bookreview.adapter.BookAdapter
import fastcampus.aop.part2.bookreview.adapter.HistoryAdapter
import fastcampus.aop.part2.bookreview.api.BookService
import fastcampus.aop.part2.bookreview.databinding.ActivityMainBinding
import fastcampus.aop.part2.bookreview.model.History
import fastcampus.aop.part2.bookreview.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        bookService.getBooksByName(getString(R.string.naver_id), getString(R.string.naver_secret_key), "fantasy")
            .enqueue(object: Callback<SearchBookDto> {
                override fun onResponse(call: Call<SearchBookDto>, response: Response<SearchBookDto>) {
                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "NOT SUCCESS!!!!")
                        return
                    }

                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        adapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })
    }

    private fun search(keyword: String) {
        bookService.getBooksByName(getString(R.string.naver_id), getString(R.string.naver_secret_key), keyword)
            .enqueue(object: Callback<SearchBookDto> {
                override fun onResponse(call: Call<SearchBookDto>, response: Response<SearchBookDto>) {
                    hideHistoryView()
                    saveSearchKeyword(keyword)

                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "NOT SUCCESS!!!!")
                        return
                    }

                    adapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    hideHistoryView()
                    Log.e(TAG, t.toString())
                }
            })
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter()

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickListener = {
            deleteSearchKeyWord(it)
        })

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }

        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()

            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
        binding.historyRecyclerView.isVisible = true
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.visibility = View.GONE
    }

    private fun deleteSearchKeyWord(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            showHistoryView()
        }.start()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}