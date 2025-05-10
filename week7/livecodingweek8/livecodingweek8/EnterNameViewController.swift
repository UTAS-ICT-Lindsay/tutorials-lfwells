//
//  ViewController.swift
//  livecodingweek8
//
//  Created by Lindsay Wells on 15/4/2025.
//

import UIKit

class EnterNameViewController: UIViewController, DataTransferDelegate
{
    @IBOutlet weak var enterNameField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    @IBAction func enteredText(_ sender: UITextField)
    {
        print("done")
        print(sender.text!)
        
        //take the manual segue to next screen
        self.performSegue(withIdentifier: "enteredNameSegue", sender: sender)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        if segue.identifier == "enteredNameSegue"
        {
            if let nextScreen = segue.destination as? DisplayNameViewController
            {
                nextScreen.goingBackDelegate = self //I will be your going back delegate sir!
                nextScreen.displayName = (sender as? UITextField)?.text ?? "Anonymous"
                
                //this will crash because its the inbetween
                //nextScreen.displayNameLabel.text = (sender as? UITextField)?.text ?? "Anonymous"
            }
        }
    }
    
    
    //implement the delegate pattern
    func receiveData(_ message: String) {
        print(message)
        enterNameField.text = (enterNameField.text ?? "") + message
    }
}

