<?php
    require('config.php');
    $json = file_get_contents('php://input');
    $obj = json_decode($json);
    $postId = $obj->{'postId'};

    $sql = "UPDATE advertisement SET recActive = 'inActive' WHERE id = '$postId' ";

    $logObj = new \stdClass();

    if ($conn->query($sql) === TRUE) 
    {
        $logObj->state = 1;
    }
    else 
    {
        // echo "Error: " . sql . "<br>" . $conn->error;
        $logObj->state = 2;
    }

    $logJson = json_encode($logObj);
    echo $logJson;
    $conn->close();
?>