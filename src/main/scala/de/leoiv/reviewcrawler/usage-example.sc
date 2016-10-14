import de.leoiv.reviewcrawler.entities.Category


// Creating a new category "Buecher"
val bookCategory = new Category("Buecher", "https://www.amazon.de/b%C3%BCcher-buch-lesen/b/ref=sd_allcat_bo?ie=UTF8&node=186606", 10)

// Getting the subcategories of this category
val subcategories = bookCategory.subcategories

// Getting all tries of books from all subcategories
val books = for(subcategory <- subcategories) yield subcategory.products

// Getting all reviews (first page only, because the parameter is set to 2) of all books
val reviews = for(bookReviewList <- books.withFilter(_.isSuccess); review <- bookReviewList.get) yield review.allReviews(2)

import slick.driver.H2Driver.api._
import slick.lifted.{ProvenShape, ForeignKeyQuery}