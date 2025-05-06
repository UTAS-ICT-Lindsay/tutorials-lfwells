//
//  ViewController.swift
//  tutorial8
//
//  Created by Lindsay Wells on 6/5/2025.
//

import UIKit

import Firebase
import FirebaseFirestore

class ViewController: UIViewController,
    UITableViewDelegate, UITableViewDataSource
{
    @IBOutlet weak var tableView: UITableView!
    var movies = [Movie]()

    override func viewDidLoad() {
        super.viewDidLoad()
        loadMovies()
    }
    override func viewDidAppear(_ animated: Bool) {
        
        loadMovies()
    }
    func loadMovies() {
        tableView.delegate = self // i want the job, assign it to me
        tableView.dataSource = self
        // Do any additional setup after loading the view.
        
        let db = Firestore.firestore()
        
        let movieCollection = db.collection("movies")
        movieCollection.getDocuments() { (result, err) in
          //check for server error
          if let err = err
          {
              print("Error getting documents: \(err)")
          }
          else
          {
              self.movies.removeAll()
              //loop through the results
              for document in result!.documents
              {
                  //attempt to convert to Movie object
                  let conversionResult = Result
                  {
                      try document.data(as: Movie.self)
                  }

                  //check if conversionResult is success or failure (i.e. was an exception/error thrown?
                  switch conversionResult
                  {
                      //no problems (but could still be nil)
                      case .success(let movie):
                          print("Movie: \(movie)")
                          self.movies.append(movie)
                          
                      case .failure(let error):
                          // A `Movie` value could not be initialized from the DocumentSnapshot.
                          print("Error decoding movie: \(error)")
                  }
              }
              
              self.tableView.reloadData()
          }
        }
        
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        movies.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "myCell", for: indexPath)

            //get the movie for this row
            let movie = movies[indexPath.row]

            //down-cast the cell from UITableViewCell to our cell class MovieUITableViewCell
            //note, this could fail, so we use an if let.
            if let movieCell = cell as? MovieTableViewCell
            {
                //populate the cell
                 movieCell.labelTitle.text = movie.title
                 movieCell.labelYear.text = String(movie.year)
            }

            return cell
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        super.prepare(for: segue, sender: sender)
        
        // is this the segue to the details screen? (in more complex apps, there is more than one segue per screen)
        if segue.identifier == "ShowMovieDetailSegue"
        {
              //down-cast from UIViewController to DetailViewController (this could fail if we didn’t link things up properly)
              guard let detailViewController = segue.destination as? DetailsViewController else
              {
                  fatalError("Unexpected destination: \(segue.destination)")
              }

              //down-cast from UITableViewCell to MovieUITableViewCell (this could fail if we didn’t link things up properly)
              guard let selectedMovieCell = sender as? MovieTableViewCell else
              {
                  fatalError("Unexpected sender: \( String(describing: sender))")
              }

              //get the number of the row that was pressed (this could fail if the cell wasn’t in the table but we know it is)
              guard let indexPath = tableView.indexPath(for: selectedMovieCell) else
              {
                  fatalError("The selected cell is not being displayed by the table")
              }

              //work out which movie it is using the row number
              let selectedMovie = movies[indexPath.row]

              //send it to the details screen
              detailViewController.movie = selectedMovie
              detailViewController.movieIndex = indexPath.row
        }
    }


}

