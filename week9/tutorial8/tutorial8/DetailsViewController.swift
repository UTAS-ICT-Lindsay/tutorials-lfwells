//
//  DetailsViewController.swift
//  tutorial8
//
//  Created by Lindsay Wells on 6/5/2025.
//

import UIKit

import Firebase
import FirebaseFirestore

class DetailsViewController: UIViewController {

    var movie : Movie?
    var movieIndex : Int? //used much later in tutorial
    
    @IBOutlet weak var titleField: UITextField!
    @IBOutlet weak var yearField: UITextField!
    @IBOutlet weak var durationField: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()

        if let displayMovie = movie
       {
           self.navigationItem.title = displayMovie.title //this awesome line sets the page title
            titleField.text = displayMovie.title
            yearField.text = String(displayMovie.year)
            durationField.text = String(displayMovie.duration)
       }
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

    @IBAction func onSave(_ sender: Any)
    {
        //(sender as! UIBarButtonItem).title = "Loading..."

        let db = Firestore.firestore()

        movie!.title = titleField.text!
        movie!.year = Int32(yearField.text!)! //good code would check this is an int
        movie!.duration = Float(durationField.text!)! //good code would check this is a float
        do
        {
            //update the database (code from lectures)
            try db.collection("movies").document(movie!.documentID!).setData(from: movie!){ err in
                if let err = err {
                    print("Error updating document: \(err)")
                } else {
                    print("Document successfully updated")
                    //this code triggers the unwind segue manually
                    //self.performSegue(withIdentifier: "saveSegue", sender: sender)
                    self.navigationController?.popViewController(animated: true)
                }
            }
        } catch { print("Error updating document \(error)") } //note "error" is a magic variable
    }
}
