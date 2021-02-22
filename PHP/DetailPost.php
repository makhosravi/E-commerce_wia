<?php
    require('config.php');
    $json = file_get_contents('php://input');
    $obj = json_decode($json);
    $postId = $obj->{'postId'};
    $phone = $obj->{'userPhone'};

    $sql = "SELECT * FROM user WHERE phoneNumber ='$phone'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) 
    {
        while ($row = $result->fetch_assoc()) 
        {
            $temp = $row["markedList"];
        }
    }

    $markedList = explode(",", $temp);
    $count = count($markedList);
    for ($i=0; $i < $count; $i++) 
    { 
        if ($markedList[$i] == $postId) 
        {
            $isChecked = "true";
        }
    }

    if ($isChecked != "true") 
    {
        $isChecked = "false";
    }

    $sql = "SELECT * FROM advertisement WHERE id = '$postId' ";

    $result = $conn->query($sql);

    if ($result->num_rows > 0) 
    {
        while ($row = $result->fetch_assoc()) 
        {
            $obj = json_decode($row['image'],TRUE);
            $img = array(
                'image1' => $obj[0], 
                'image2' => $obj[1],
                'image3' => $obj[2],
                'image4' => $obj[3],
                'image5' => $obj[4]);
            
            $data = new \stdClass();
            $data-> id = $row["id"];
            $data->userId = $row["userId"];
            $data->categoryId = $row["categoryId"];
            $data->city = $row["city"];
            $data->area = $row["area"];
            $data->price = $row["price"];
            $data->orderType = $row["orderType"];
            $data->advertiserType = $row["advertiserType"];
            $data->numOfRoom = $row["numOfRoom"];
            $data->isCountrySide["isCountrySide"];
            $data->trust = $row["trust"];
            $data->rent = $row["rent"];
            $data->officialDox = $row["officialDoc"];
            $data->image = $img;
            $data->adTitle = $row["adTitle"];
            $data->adDate = $row["adDate"];
            $data->adExplain = $row["adExplain"];
            $data->lifeTime = $row["lifeTime"];
            $data->isChecked = $isChecked;
            $data->isEmergency = $row["isEmergency"];
        }
    }
    else 
    {
        $logObj = new \stdClass();
        $logObj->state = 2;
        $check = 1;
        $logJson = json_encode($logObj);
        echo $logJson;
    }
    if ($check != 1) 
    {
        echo json_encode($data);
    }
    $conn->close();
?>