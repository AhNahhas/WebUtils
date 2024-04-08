# WebUtils

A utility Java library for a cleaner & simpler code.

## Installation

Maven dependency [Sonatype Central Repository](https://central.sonatype.com/artifact/io.github.ahnahhas/webutils)

## Examples



```java
//with webutils 
var iterator = OptionalUtils.ofMappable(collection, Collection::iterator);

//with plain java
var iterator = collection != null ? collection.iterator() : null;
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
Comparator<String> comparator = (a, b) -> StringUtils.compareIgnoreCase(a, b);
Comparator<String> nullFriendlyComparator = CollectionUtils.nullFirstComparator(comparator);
var list = stream.filter(StreamUtils.filterDuplicate(nullFriendlyComparator))
                 .collect(Collectors.toList());
```

## Contributing

Pull requests are welcome if you want to contribute to this library.

## License

[MIT](https://choosealicense.com/licenses/mit/)
