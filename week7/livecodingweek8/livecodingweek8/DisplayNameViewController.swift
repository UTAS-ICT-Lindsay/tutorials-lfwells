//
//  DisplayNameViewController.swift
//  livecodingweek8
//
//  Created by Lindsay Wells on 15/4/2025.
//

import UIKit

class DisplayNameViewController: UIViewController {
    
    var displayName: String?
    
    @IBOutlet weak var displayNameLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()

        displayNameLabel.text = displayName
    }
}
