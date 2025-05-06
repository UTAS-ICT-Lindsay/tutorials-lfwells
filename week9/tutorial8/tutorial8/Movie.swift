//
//  Movie.swift
//  tutorial8
//
//  Created by Lindsay Wells on 6/5/2025.
//

import Firebase
import FirebaseFirestore

public struct Movie : Codable
{
    @DocumentID var documentID:String?
    var title:String
    var year:Int32
    var duration:Float
    var teamAPlayers:[String]? = ["lindsay", "bob"]
}
