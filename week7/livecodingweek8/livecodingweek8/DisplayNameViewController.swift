//
//  DisplayNameViewController.swift
//  livecodingweek8
//
//  Created by Lindsay Wells on 15/4/2025.
//

import UIKit

protocol DataTransferDelegate
{
    func receiveData(_ message: String)
}



class DisplayNameViewController: UIViewController {
    
    var displayName: String?
    
    var goingBackDelegate: DataTransferDelegate?
    
    @IBOutlet weak var displayNameLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()

        displayNameLabel.text = displayName
    }
    
    @IBAction func characterButtonPressed(_ sender: UIButton)
    {
        print(sender.titleLabel?.text)
        
        //remember to use the goingBackDelegate here in a sec
        goingBackDelegate?.receiveData(sender.titleLabel!.text!)
        
        //equivalent of finish() in android
        self.navigationController?.popViewController(animated: true)
    }
    
}
