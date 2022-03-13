package fastcampus.aop.part2.bookreview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import fastcampus.aop.part2.bookreview.databinding.ActivityDetailBinding
import fastcampus.aop.part2.bookreview.model.Book
import fastcampus.aop.part2.bookreview.model.Review

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val model = intent.getParcelableExtra<Book>("bookModel")

        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.image.orEmpty())
            .into(binding.coverImageView)

        Thread {
            val review: Review? = db.reviewDao().getOneReview(model?.id.orEmpty())
            runOnUiThread {
                binding.reviewEditTextView.setText(review?.review.orEmpty())
            }
        }.start()

        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(
                        model?.id.orEmpty(),
                        binding.reviewEditTextView.text.toString())
                )
            }.start()
        }

    }
}