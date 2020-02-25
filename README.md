# imageio-client

## Deploy and Run
    $ mvn clean package
    $ java -jar target/imageio-client-1.0-SNAPSHOT-jar-with-dependencies.jar ...

## Usage
    $ imageio_client PUT <ticket_json>
    $ imageio_client GET <ticket_uuid>
    $ imageio_client DELETE <ticket_uuid>
    
## Examples
    $ alias imageio_client="java -jar target/imageio-client-1.0-SNAPSHOT-jar-with-dependencies.jar"
    $ imageio_client PUT 
        "{
            'uuid': '799030aa-97c3-4354-871e-0adf0556fcbf',
            'size': 1073741824,
            'url': 'file://dev/vgname/lvname',
            'timeout': 3000,
            'ops': ["read", "write"]
        }"
    $ imageio_client GET 799030aa-97c3-4354-871e-0adf0556fcbf
    $ imageio_client DELETE 799030aa-97c3-4354-871e-0adf0556fcbf
