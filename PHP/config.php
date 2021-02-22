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
?>