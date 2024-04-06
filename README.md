# WebUtils

A utility Java library for a cleaner & simpler code.

## Installation

Maven dependency [Sonatype Central Repository](https://central.sonatype.com/artifact/io.github.ahnahhas/webutils)

## Examples



```java
//with webutils 
var iterator = OptionalUtils.ofMappable(firstCollection, Collection::iterator);

//with plain java
var iterator = firstCollection != null ? firstCollection.iterator() : null;
```

``` java
//find mutual objects in both collections 
List<String> listOne = List.of("One", "Two", "Three");
List<String> listTwo = List.of("Three", "Four", "Five");

//contains "Three"
var result = CollectionUtils.innerJoin(listOne, listTwo);          
```

``` java
//filter stream elements using a predicate
var list = stream.filter(StreamUtils.filterDuplicate(comparator))
                 .collect(Collectors.toList());
```

## Contributing

Pull requests are welcome if you want to contribute to this library.

## License

[MIT](https://choosealicense.com/licenses/mit/)
