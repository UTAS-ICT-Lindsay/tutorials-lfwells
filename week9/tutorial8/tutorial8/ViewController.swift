//
//  ViewController.swift
//  tutorial8
//
//  Created by Lindsay Wells on 6/5/2025.
//

import UIKit

import Firebase
import FirebaseFirestore

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        let db = Firestore.firestore()
        
        let movieCollection = db.collection("movies")
        let matrix = Movie(title: "The Matrix", year: 1999, duration: 150)
        
        do {
            var something = try movieCollection.addDocument(from: matrix, completion: { (err) in
                
                guard err == nil else {
                    print("Error adding document: \(err!)")
                    return
                }
                
                print("Successfully created movie")
            })
            print("do we have a document id? \(something.documentID)")
        } catch let error {
            print("Error writing city to Firestore: \(error)")
        }
    }


}

