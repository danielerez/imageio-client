# imageio-client

## Build

    $ mvn clean package

## Test

    $ mvn clean test

## Usage

    $ imageio_client sock PUT <ticket_json>
    $ imageio_client sock GET <ticket_uuid>
    $ imageio_client sock DELETE <ticket_uuid>

## Examples

Adding a ticket:

    $ java -jar target/imageio-client-1.0-SNAPSHOT-jar-with-dependencies.jar \
        /path/to/sock \
        PUT '{
            "uuid": "799030aa-97c3-4354-871e-0adf0556fcbf",
            "size": 1073741824,
            "url": "file:///path/to/image",
            "timeout": 3000,
            "ops": ["read", "write"]
        }'

Get ticket info:

    $ java -jar target/imageio-client-1.0-SNAPSHOT-jar-with-dependencies.jar \
        /path/to/sock \
        GET 799030aa-97c3-4354-871e-0adf0556fcbf

Delete a ticket:

    $ java -jar target/imageio-client-1.0-SNAPSHOT-jar-with-dependencies.jar \
        /path/to/sock \
        DELETE 799030aa-97c3-4354-871e-0adf0556fcbf
