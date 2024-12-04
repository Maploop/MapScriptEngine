# MapScriptEngine

MapScriptEngine is a interpreted programming language that's been created for fun and for personal use in the future.

# Features
- [x] Functions
- [x] Local Variables
- Binary operations
  - [x] Addition
  - [x] Decreimention
  - [x] Multiplication
  - [x] Division
- [ ] Void Type Functions
- [x] String Appending (+)
- [ ] Classes
- [ ] Respectable Variable Types
- [ ] Global Variables
- [ ] Code Comments


## Syntax & Examples
Example code can be found in the `main.maps` file.
```
fn addFunction(a: int, b: int) => int {
  return a + b
}

fn addOrSome() => int {
    var added: int = addFunction(10, 20)
    sys.println(added)
    return added
}

fn main() => int {
  var finalAdded: int = addOrSome()
  sys.println("final added is " + finalAdded)
  var division: int = finalAdded / 2
  sys.println("devided is " + division)
  return 0
}
```
Since variable types are not considered with the keywords yet, setting variable types such as `var myVar: int` is not neccessary, but has been put into this example for future reference.
`fn main() => int` is the main function of every program.

# Usage
### Clone
```powershell
git clone https://github.com/Maploop/MapScriptEngine.git
```
### Compile
```powershell
mvn clean package
```
### Run
Make a file with the `.maps` extension, then simply run the following command:
```powershell
java -jar MapScriptEngine.jar ./path/to/.maps/file
```

# Contributing
Feel free to contribute to the project either adding in the missing features or improving existing ones! I will personally review pull requests.
