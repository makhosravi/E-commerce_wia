<?php
    require('config.php');
    $json = file_get_contents('php://input');
    $obj = json_decode($json);
    $phone = $obj->{'userPhone'};

    $sql = "SELECT * FROM user WHERE phoneNumber = '$phone' ";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) 
    {
        while ($row = $result->fetch_assoc()) 
        {
            $temp = $row["markedList"];
        }
    }

    $sql = "SELECT * FROM advertisement WHERE id = ";
    $array = explode(",",$temp);
    $count = count($array);
    $c = 0;
    for ($i=0; $i < ($count - 1); $i++) 
    { 
        if ($c == 0) 
        {
            $c = $c + 1;
            $sql = $sql . $array[$i];
        }
        else {
            $sql = $sql . " OR id = " . $array[$i];
        }
    }

    $sql = $sql . " ORDER BY id DESC";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) 
    {
        while ($row = $result->fetch_assoc()) 
        {
            if ($row["recActive"] == "Active") 
            {
                $obj = json_decode($row["image"],TRUE);
                $img = array(
                    'image1' => $obj[0],
                    'image2' => $obj[1],
                    'image3' => $obj[2],
                    'image4' => $obj[3],
                );
                $data[] = array(
                    'id' => $row["id"],
                    'userId' => $row["userId"],
                    'price' => $row["price"],
                    'image' => $img,
                    'adTitle' => $row["adTitle"],
                    'adDate' => $row["adDate"],
                    'isEmergency' => $row["isEmergency"],
                );
            }
        }
    }
    else {
        $logObj[] = array(
            'state' => "2",
        );
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