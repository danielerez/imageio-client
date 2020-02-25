# imageio-client

## Deploy and Run
    $ mvn clean package
    $ java -jar target/imageio-client-1.0-SNAPSHOT-jar-with-dependencies.jar ...

## Usage
    $ client PUT <ticket_json>
    $ client GET <ticket_uuid>
    $ client DELETE <ticket_uuid>
    
## Examples
    $ client PUT 
        "{
            'uuid': '799030aa-97c3-4354-871e-0adf0556fcbf',
            'size': 1073741824,
            'url': 'file://dev/vgname/lvname',
            'timeout': 3000,
            'ops': ["read", "write"]
        }"
    $ client GET 799030aa-97c3-4354-871e-0adf0556fcbf
    $ client DELETE 799030aa-97c3-4354-871e-0adf0556fcbf