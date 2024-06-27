package com.starkindustries.authentication.Model
open class Notes
{
    lateinit var title:String
    lateinit var thought:String
    lateinit var noteId:String
    constructor(title_: String,thought_: String,noteId_:String)
    {
        this.title=title_
        this.thought=thought_
        this.noteId=noteId_
    }
    constructor()
    {

    }
}