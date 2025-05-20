import 'dart:io';

import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:week12_live_demo/movie_details.dart';
import 'package:week12_live_demo/movie_model.dart';
import 'package:week12_live_demo/take_picture_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context)
  {
    return ChangeNotifierProvider(
      create: (_) => MovieModel(),
      child: MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        //home: const MyHomePage(title: 'List Tutorial'),
        routes: {
          //default route
          '/': (context) => const MyHomePage(title: 'List Tutorial'),
          //movie details with id parameter
          '/movieDetails': (context) {
            final id = ModalRoute.of(context)!.settings.arguments as int;
            return MovieDetails(id: id);
          },
        },
        initialRoute: '/',
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage>
{
  String? picturePath = null;

  @override
  Widget build(BuildContext context) {
    return Consumer<MovieModel>(
      builder: (context, movieModel, _) {
        return Scaffold(
          appBar: AppBar(
            title: Text(widget.title),
            backgroundColor: Theme.of(context).colorScheme.inversePrimary,
          ),
          body: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[

                if (picturePath == null)
                  Text("Take a picture mate use that cool button below")
                else
                  Image.file(File(picturePath!)),

                Expanded(
                  child: ListView.builder(
                      itemBuilder: (_, index) {
                        var movie = movieModel.items[index];
                        final image = movie.image;
                        return ListTile(
                          title:Text(movie.title),
                          subtitle: Text("${movie.year} - ${movie.duration} Hours"),
                          trailing: image != null ? Image.network(image) : null,
                          onTap: () {
                            //Navigator.push(context, MaterialPageRoute(builder: (context) { return MovieDetails(id:index); }));
                            Navigator.pushNamed(context, "/movieDetails", arguments: index);
                          },
                        );
                      },
                      itemCount:movieModel.items.length
                  ),
                )


              ],
            ),
          ),
          floatingActionButton: FloatingActionButton(onPressed: () async {
            final cameras = await availableCameras();
            // Get a specific camera from the list of available cameras.
            final firstCamera = cameras.first;
            var pathResult = await Navigator.push<String>(
                context,
                MaterialPageRoute(
                  builder: (context) => TakePictureScreen(
                    // Pass the appropriate camera to the TakePictureScreen widget.
                    camera: firstCamera
                  )
                ));

            if (pathResult != null)
            {
              setState(() {
                picturePath = pathResult;
              });
            }
          }, child: Icon(Icons.camera_alt)),
        );
      }
    );
  }
}