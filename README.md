# Spring Json Views

Provides support for using Jackson's JsonView in controllers, either with an annotation (`ResponseView`) 
or with a wrapped object (implementing `DataView`), the latter of which is useful when you want
to conditionally chose which view to display (say, perhaps, depending on a request parameter). 

### Example: 

```
@RestController
public class ExampleController {

    // returns: {"name":"dave","foobar":"pass"}
    @RequestMapping("/dummy1")
    public ResponseEntity<?> dummy1(){
        return new ResponseEntity<Object>(new Dummy("dave", "pass"), HttpStatus.OK);
    }

    // returns: {"name":"dave"}
    @ResponseView(Summary.class)
    @RequestMapping("/dummy2")
    public ResponseEntity<?> dummy2(){
        return new ResponseEntity<Object>(new Dummy("dave", "pass"), HttpStatus.OK);
    }

    // returns: {"name":"dave"}
    @RequestMapping("/dummy3")
     public ResponseEntity<?> dummy3(){
        return new JsonViewResponseEntity<Object>(Summary.class, new Dummy("dave", "pass"), HttpStatus.OK);
    }

    // returns: {"name":"dave"}
    @RequestMapping("/dummy4")
    public DataView<Dummy> dummy4(){
        return new DataViewImpl<Dummy>(new Dummy("dave", "pass"), Summary.class);
    }

    public interface Summary extends BaseView {}

    public class Dummy{
        @JsonView(Summary.class)
        private final String name;
        private final String foobar;

        public Dummy(String name, String foobar) {
            this.name = name;
            this.foobar = foobar;
        }

        public String getName() {
            return name;
        }

        public String getFoobar() {
            return foobar;
        }
    }
}
```
