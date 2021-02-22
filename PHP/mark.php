<?php
    require('config.php');
    $json = file_get_contents('php://input');
    $obj = json_decode($json);
    $userPhone = $obj->{'userPhone'};
    $postId = $obj->{'postId'};

    $sql = "SELECT * FROM user WHERE phoneNumber = '$userPhone' ";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) 
    {
        while($row = $result->fetch_assoc())
        {
            $temp = $row["markedList"];
        }
    }

    $markedList = array_filter(explode(",", $temp));
    $count = count($markedList);

    $c=false;
    $l=0;
    $logObj = new \stdClass();
    for ($i=0; $i < $count; $i++) 
    { 
        if ($markedList[$i] == $postId) 
        {
            
            $l=$i;
            $c = true;
        }
    }

    if ($c) 
    {
        array_splice($markedList, $l, 1);
        $count = count($markedList);
        $newTemp = "";
        for ($i=0; $i < $count; $i++) 
        { 
            $newTemp .= $markedList[$i] . ",";
        }
        $sql = "UPDATE user SET markedList = '$newTemp' WHERE phoneNumber = '$userPhone' ";
        $result = $conn->query($sql);
        if ($result) 
        {
            $logObj->status = "unmarked";
        }
        else
        {
            $logObj->status = 2;
        }
    }

    else if (!$c) 
    {
        $temp .= $postId . ",";
        $sql = "UPDATE user SET markedList = '$temp' WHERE phoneNumber = '$userPhone' ";
        $result = $conn->query($sql);
        if ($result) 
        {
            $logObj->status = "marked";
        }
        else
        {
            $logObj->status = 2;
        }
    }

    echo json_encode($logObj);
?>