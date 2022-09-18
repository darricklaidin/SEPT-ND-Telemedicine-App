import 'package:flutter/material.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    double height = MediaQuery.of(context).size.height;
    double width = MediaQuery.of(context).size.width;
    Color color = Theme.of(context).colorScheme.primary;

    return Scaffold(
      body: _buildBody(color, height, width),
    );
  }

  Widget _buildBody(color, height, width) {
    return SizedBox(
      height: height,
      width: width,
      child: Stack(
        children: <Widget>[
          Positioned(
            top: 0,
            width: width,
            height: 100,
            child: _buildSearchBar(color, height, width),
          ),
          Positioned(
            top: 110,
            width: width,
            height: height - 100,
            child: _buildContent(height, width),
          ),
        ],
      ),
    );
  }

  Widget _buildSearchBar(color, height, width) {
    return Container(
      color: color,
      child: SafeArea(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Container(
              width: width - 40,
              height: 40,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(5),
              ),
              child: const TextField(
                decoration: InputDecoration(
                  border: InputBorder.none,
                  enabledBorder: InputBorder.none,
                  focusedBorder: InputBorder.none,
                  prefixIcon: Icon(
                    Icons.search,
                    color: Colors.black87,
                  ),
                  hintText: 'Search doctors...',
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildContent(height, width) {
    return SizedBox(
      width: width,
      height: height - (height * .35) + 50,
      child: LayoutBuilder(
        builder: (BuildContext c, BoxConstraints constraints) {
          return ListView(
            padding: const EdgeInsets.only(left: 20),
            children: const [],
          );
        },
      ),
    );
  }
}