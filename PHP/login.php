<?php
    $servername = "localhost:3306";
    $username = "serverte_makh";
    $password = "1342makh1342";
    $dbname = "serverte_divar";
    // create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // check connection
    if($conn->connect_error)
    {
        die("Connection failed : " . $conn->connect-error);
    }

    if(!$conn->set_charset("utf8"))
    {
        printf("Error loading character set utf8", $mysqli->error);
    }
    else
    {
        $conn->character_set_name();
    }

    $json = file_get_contents('php://input');
    $obj = json_decode($json);
    $phoneNumber= $obj->{'phoneNumber'};
    // $phoneNumber = $_REQUEST['phoneNumber'];
    
    $logObj = new \stdClass();

    $sql = "SELECT * FROM user WHERE phoneNumber='$phoneNumber' ";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $logObj->state=1; //tekrary
        }
    }
    else{
        $sql = "INSERT INTO user (phoneNumber) VALUES ('$phoneNumber')";

        if ($conn->query($sql) === TRUE) {
            $logObj->state=2; //sabt shod
        }else{
            //echo "Error: " . $sql . "<br>" . $conn->error;
            $logObj->state=3; // sabt nashod
        }
    }

    $logJson = json_encode($logObj);
    echo $logJson;
    $conn->close();
?>