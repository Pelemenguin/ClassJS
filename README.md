ClassJS is a mod to create Java classes in KubeJS.

## Basic Example

You may operate Java byte code to build your own methods.

**Note:** Some instructions that interacts with classes (`getstatic`, `invokestatic`, `new`, etc.) is protected under KubeJS's class filters. You may not use them to access classes that is not allowed by KubeJS or its other addons. Neither can you extend or implement them.

```javascript
let ExampleClass = ClassCreator.create("ExampleClass")
    .toPublic()           // Set the class to public
    .defaultConstructor() // Add a default constructor
    .createMethod("exampleMethod", [], "java.lang.String") // Method name, parameter types, and return type
        .toPublic()
        .code(builder => {
            builder.pushString("Hello, ClassJS!")   // Push a string reference to the operand stack
                .returnObject();                    // Returns the string
        })
    .defineClass();     // Define the class

console.info((new ExampleClass()).exampleMethod());
```

Equivalent Java code:

```java
public class ExampleClass {

    public ExampleClass() {
        super();
    }

    public String exampleMethod() {
        return "Hello, ClassJS!";
    }

}
```

## Code in JavaScript

If you do not want to learn Java byte code, you can create methods using JavaScript functions.

```javascript
let ExampleClass = ClassCreator.create("ExampleClass")
    .toPublic()
    .createMethod("exampleMethod", ["int", "int"], "int")
        .toPublic().toStatic()
        .codeJS((num1, num2) => {
            return num1 + num2;
        })
    .defineClass();

console.info(ExampleClass.exampleMethod(1, 2));
// Output in startup.log
// 3.0
```

Equivalent Java code:

```java
public ExampleClass {
    
    // Note: This class do not even have a constructor,
    //         as the JavaScript above did not call `defaultConstructor`,
    //         neither did it created any other constructors.

    public static int exampleMethod(int i1, int i2) {
        return i1 + i2;
    }

}
```

## Load a Created Class

Classes can be only defined in *startup* scripts.
If you want to use them in other scripts, you can use `ClassJSUtils.loadClass` method.

```javascript
ClassCreator.create("SomeClass")
    /** Do some operations to add content to this class */
    .defineClass();

// In other scripts:

let SomeClass = ClassJSUtils.loadClass("SomeClass");
// Then you can use `SomeClass`
```
