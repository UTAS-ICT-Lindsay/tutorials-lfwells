import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:week12_live_demo/movie_model.dart';

class MovieDetails extends StatefulWidget {
  const MovieDetails({super.key, required this.id});

  final int id;

  @override
  State<MovieDetails> createState() => _MovieDetailsState();
}

class _MovieDetailsState extends State<MovieDetails> {
  final _formKey = GlobalKey<FormState>();
  final titleController = TextEditingController();
  final yearController = TextEditingController();
  final durationController = TextEditingController();

  @override
  Widget build(BuildContext context)
  {
    return Consumer<MovieModel>(
      builder: (context, movieModel, _) {

        var movies = movieModel.items;
        var movie = movies[widget.id];

        titleController.text = movie.title;
        yearController.text = movie.year.toString();
        durationController.text = movie.duration.toString();

        return Scaffold(
            appBar: AppBar(
              title: const Text("Edit Movie"),
              backgroundColor: Theme.of(context).colorScheme.inversePrimary,
            ),
            body: Padding(
                padding: const EdgeInsets.all(8),
                child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: <Widget>[
                      Text("Movie Index ${widget.id}"),
                      Form(
                        key: _formKey,
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Column(
                            children: <Widget>[
                              TextFormField(
                                decoration: const InputDecoration(labelText: "Title"),
                                controller: titleController,
                                autofocus: true,
                              ),
                              TextFormField(
                                decoration: const InputDecoration(labelText: "Year"),
                                controller: yearController,
                              ),
                              TextFormField(
                                decoration: const InputDecoration(labelText: "Duration"),
                                controller: durationController,
                              ),
                              Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: ElevatedButton.icon(onPressed: () {
                                  if (_formKey.currentState?.validate() ?? false)
                                  {
                                    //update the movie object
                                    movie.title = titleController.text;
                                    movie.year = int.parse(yearController.text); //good code would validate these
                                    movie.duration = double.parse(durationController.text); //good code would validate these

                                    //update the model
                                    movieModel.update();

                                    //return to previous screen
                                    Navigator.pop(context);
                                  }
                                }, icon: const Icon(Icons.save), label: const Text("Save Values")),
                              )
                            ],
                          ),
                        ),
                      )
                    ]
                )
            )
        );
      }
    );
  }
}
