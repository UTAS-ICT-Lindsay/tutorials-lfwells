//
//  MovieTableViewCell.swift
//  tutorial8
//
//  Created by Lindsay Wells on 6/5/2025.
//

import UIKit

class MovieTableViewCell: UITableViewCell
{
    @IBOutlet weak var labelTitle: UILabel!
    
    @IBOutlet weak var labelYear: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
