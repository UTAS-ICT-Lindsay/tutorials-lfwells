import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const FirstPage(),
    );
  }
}

class FirstPage extends StatefulWidget {
  const FirstPage({Key? key}) : super(key: key);

  @override
  State<FirstPage> createState() => _FirstPageState();
}

class _FirstPageState extends State<FirstPage> {

  int selectedDropdownItem = 1;

  Map<int, String> dropdownItems = {
    1: "Frodo",
    2: "Nida",
    3: "Nidoran",
  };

  @override
  Widget build(BuildContext context)
  {
    //higher order function way
    /*var items = dropdownItems.entries.map((keyValuePair) {
      return DropdownMenuItem(
        value: keyValuePair.key,
        child: Text(keyValuePair.value),
      );
    }).toList();*/

    //normal for loop way
    var items = <DropdownMenuItem<int>>[];
    for (var key in dropdownItems.keys) {
      items.add(
        DropdownMenuItem(
          value: key,
          child: Text(dropdownItems[key]!),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text("My Flutter App"),
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [

            DropdownButton<int>(
              value: selectedDropdownItem,
                items: items,
                onChanged: (newValue) {
                  setState(() {
                    selectedDropdownItem = newValue!;
                  });
                }
            ),
Text(selectedDropdownItem.toString()),
            ElevatedButton.icon(
              icon: const Icon(Icons.add),
              label: const Text("Save"),
              onPressed: () =>
              {
                //Navigator.pushNamed(context, "/products/9001"),

                Navigator.push(context, MaterialPageRoute(
                    builder:(context) => SecondPage(name:dropdownItems[selectedDropdownItem]!)
                ))
              },
            ),
          ],
        ),
      ),
    );
  }
}

class SecondPage extends StatelessWidget {

  final String name;

  const SecondPage({Key? key, required this.name}) : super(key: key);

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        title: const Text("Second Screen"),
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
      ),
      body: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Text(name),
      ),
    );
  }
}